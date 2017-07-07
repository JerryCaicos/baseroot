package com.base.application.baseapplication.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.base.application.baseapplication.utils.LogUtils;
import com.base.application.baseapplication.utils.WebpUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 图片加载器.
 *
 * @Title:
 * @Description:
 * @Author:12075179
 * @Since:2014-8-15
 * @Version:
 */
public class ImageLoader
{
	/**
	 * LOG TAG
	 **/
	private static final String TAG = "ImageLoader";

	/**
	 * 最大内存大小[KB]
	 **/
	private static final int MAX_MEMORY_SIZE = 20 * 1024;

	/**
	 * 加载两张相同图片的时效, 大于这个值, 则需要重新加载
	 **/
	private static final long MIN_LOAD_SAME_TIME = 500;

	/**
	 * tag key, 用于在ImageView存储图片地址
	 **/
	private static final int KEY_TAG_IMAGE_URL = 0xff000000;
	/**
	 * tag key, 用于在ImageView存储图片请求
	 **/
	private static final int KEY_TAG_IMAGE_POOL = 0xff000001;
	/**
	 * tag key, 用于ImageView加载图片的时间
	 **/
	private static final int KEY_TAG_IMAGE_LOAD_TIME = 0xff000002;

	/**
	 * 最大保存线程数量
	 **/
	private static final int MAX_SAVE_POOL_SIZE = 5;
	/**
	 * 最大请求线程数量
	 **/
	private static final int MAX_REQUEST_POOL_SIZE = 5;

	/**
	 * 默认图片加载ID
	 **/
	private static int DEFAULT_LOAD_IMAGE_ID = -1;

	/**
	 * 内存缓存, 不公用
	 **/
	private BasicLruCache mMemoryCache;

	/**
	 * SD卡缓存
	 **/
	private BasicDiskCache mDiskLruCache;

	/**
	 * 线程池, 用于请求图片
	 **/
	private ThreadPoolExecutor mRequestExecutor;
	/**
	 * 线程池, 用于文件保存
	 **/
	private ThreadPoolExecutor mSaveExecutor;

	/**
	 * 消息Handler, 用于非UI线程 提交图片加载结果 → UI线程
	 **/
	private LoadHandler mHandler;

	/**
	 * 加载过程中的默认图片ID
	 **/
	private int mLoadingImageId;

	private OkUrlFactory mOkUrlFactory;

	/**
	 * 缓存方式.<BR>
	 * <BR>
	 * {@link CacheType#ONLY_MEMORY}: 只缓存内存中;<BR>
	 * {@link CacheType#ONLY_SDCARD}: 只缓存SD卡;<BR>
	 * {@link CacheType#MEMORY_SDCARD}: 缓存内存&SD卡中.
	 *
	 * @Title:
	 * @Description:
	 * @Author:12075179
	 * @Since:2014-8-25
	 * @Version:
	 */
	public enum CacheType
	{
		ONLY_MEMORY,
		// 只缓存内存
		ONLY_SDCARD,
		// 只缓存SD卡
		MEMORY_SDCARD;// 内存,SD卡都缓存.
	}

	/**
	 * 缓存方式 {@link CacheType}
	 **/
	private CacheType mCacheType = CacheType.MEMORY_SDCARD;

	/**
	 * 构造方法.
	 *
	 * @param context {@link Context}
	 */
	public ImageLoader(Context context)
	{
		this(context,DEFAULT_LOAD_IMAGE_ID);
	}

	/**
	 * 构造方法.
	 *
	 * @param context        {@link Context}
	 * @param loadingImageId 加载过程中的默认图片ID.
	 */
	public ImageLoader(Context context,int loadingImageId)
	{
		// 默认加载ID
		this.mLoadingImageId = loadingImageId;

		// 1, 初始化【内存缓存】
		int cacheSize = getMemoryCacheSize(context);
		LogUtils.d(TAG,"memory cache size [KB] : " + cacheSize);
		mMemoryCache = new BasicLruCache(cacheSize);

		// 2,初始化【SD卡缓存】
		mDiskLruCache = BasicDiskCache.getInstance(context);

		// 3,初始化【请求线程池】+ 【保存线程池】
		mRequestExecutor = new ThreadPoolExecutor(MAX_REQUEST_POOL_SIZE,MAX_REQUEST_POOL_SIZE,
				0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
		mSaveExecutor = new ThreadPoolExecutor(MAX_SAVE_POOL_SIZE,MAX_SAVE_POOL_SIZE,
				0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());

		// 4, 初始化回调handler
		mHandler = new LoadHandler(this);

		mOkUrlFactory = new OkUrlFactory((new OkHttpClient()));
	}

	/**
	 * @param context
	 * @return 内存缓存大小
	 */
	private int getMemoryCacheSize(Context context)
	{

		if(context == null)
		{
			return MAX_MEMORY_SIZE;
		}

		Object systemService = context.getSystemService(Context.ACTIVITY_SERVICE);
		if(systemService != null && systemService instanceof ActivityManager)
		{
			ActivityManager am = (ActivityManager)systemService;
			ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
			am.getMemoryInfo(mi);
			// 设置图片缓存大小为程序最大可用内存的1/4, 1024 → 10, 4 → 2.
			int cacheSize = (int)(mi.availMem >> 12);
			if(cacheSize > MAX_MEMORY_SIZE)
			{
				// 如果大于最大缓存值, 则设置为最大缓存值
				cacheSize = MAX_MEMORY_SIZE;
			}
			return cacheSize;
		}
		return MAX_MEMORY_SIZE;
	}

	/**
	 * 设置bitmap的保存格式.
	 *
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-8-15
	 */
	public void setBitmapCompressFormat(Bitmap.CompressFormat type)
	{
		// asyncImageLoader.setBitmapCompressFormat(type);
		LogUtils.w(this,"do not support bitmap compress format.");
	}

	/**
	 * 设置{@link #mCacheType}
	 *
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-8-25
	 */
	public void setBitmapCacheType(CacheType type)
	{
		mCacheType = type;
	}

	/**
	 * 设置默认图片加载ID
	 *
	 * @Author 12075179
	 * @Date 2015-11-6
	 */
	public static void setDefaultLoadImageId(int id)
	{
		DEFAULT_LOAD_IMAGE_ID = id;
	}

	/**
	 * <B>ImageView加载图片, {@link ImageView#setImageBitmap(Bitmap)}<B>
	 *
	 * @param imageUrl  网络图片地址;
	 * @param imageView 显示图片的ImageView<BR/>
	 *                  占用 {@link ImageView#setTag(int,Object)}, key为 {@link #KEY_TAG_IMAGE_URL};
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-8-15
	 */
	public void loadImage(final String imageUrl,final ImageView imageView)
	{
		loadImage(imageUrl,imageView,mLoadingImageId);
	}

	/**
	 * <B>ImageView加载图片, {@link ImageView#setImageBitmap(Bitmap)}<B>
	 *
	 * @param imageUrl       网络图片地址;
	 * @param imageView      显示图片的ImageView<BR/>
	 *                       占用 {@link ImageView#setTag(int,Object)}, key为 {@link #KEY_TAG_IMAGE_URL};
	 * @param loadingImageId 加载过程中的默认图片ID.
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-8-15
	 */
	public void loadImage(final String imageUrl,final ImageView imageView,final int loadingImageId)
	{
		loadImage(imageUrl,imageView,loadingImageId,null);
	}

	/**
	 * <B>ImageView加载图片, {@link ImageView#setImageBitmap(Bitmap)}<B>
	 *
	 * @param imageUrl       网络图片地址;
	 * @param imageView      显示图片的ImageView<BR/>
	 *                       占用 {@link ImageView#setTag(int,Object)}, key为 {@link #KEY_TAG_IMAGE_URL};
	 * @param loadingImageId 加载过程中的默认图片ID.
	 * @param loadListener   {@link OnLoadCompleteListener}
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-8-15
	 */
	public void loadImage(final String imageUrl,final ImageView imageView,
			final int loadingImageId,final OnLoadCompleteListener loadListener)
	{
		// 如果< ImageView >为空,或者< 图片地址 >为空,直接返回.
		if(imageView == null || TextUtils.isEmpty(imageUrl))
		{
			return;
		}

		// 1,从内存中获取
		String lastImageUrl = putImageUrlToTag(imageView,imageUrl);

		if(!isNeedLoad(imageView,lastImageUrl,imageUrl))
		{
			LogUtils.d("MSG","in loading, no need to reload : " + imageUrl);
			return;
		}
		Bitmap bitmap = getBitmapFromMemoryCache(lastImageUrl,imageUrl);
		if(bitmap != null && !bitmap.isRecycled())
		{
			if(loadListener != null)
			{
				// listener不为空,说明调用者想自己处理加载结果
				loadListener.onLoadComplete(bitmap,imageView,imageUrl,
						new ImageLoadedParams(ImageLoadedParams.DURATION_MEMORY));
			}
			else
			{
				// 否则,显示图片
				LogUtils.i(TAG,"memory hit , show bitmap: " + imageUrl);
				imageView.setImageBitmap(bitmap);
			}
			return;
		}

		// 2,如果内存获取失败,则从SD卡 or 网络中获取
		if(loadingImageId > 0)
		{
			// 先显示默认加载的图片.
			imageView.setImageResource(loadingImageId);
		}

		// 2.1 如果线程挂掉了,其实就是人为的调用清空缓存那个操作,说明不需要加载了,直接返回null
		if(mRequestExecutor.isShutdown())
		{
			// 不用图片了,也无须使用回调
			return;
		}

		// 2.2 开启线程,从SD卡 or 网络中获取
		Runnable requestRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				ImageLoaderResult result = null;

				// 2.3 从SD卡中获取
				Bitmap bitmap = getBitmapFromDiskCache(imageUrl);

				// 2.4 如果SD中获取失败,则从网路中获取
				if(bitmap == null)
				{
					result = loadNetImageAndSaveIfNeed(imageUrl);
				}
				else
				{
					result = new ImageLoaderResult(bitmap,new ImageLoadedParams(
							ImageLoadedParams.DURATION_SDCARD));
				}

				// 2.7 将Bitmap对象添加到内存缓存当中
				if(result.bitmap != null)
				{
					addBitmapToMemoryCache(imageUrl,result.bitmap);
				}

				// 2.8 判断图片是否发生改变
				// 如果加载好的图片地址,和<ImageView>加载的地址不同,
				// 说明这个<ImageView>又开启了别的图片加载,取消显示当前的图片.
				if(isImageUrlChanged(imageView,imageUrl))
				{
					return;
				}

				// 2.9 显示图片
				mHandler.postLoaderResult(imageUrl,imageView,null,result);
			}
		};
		executeImageLoadTask(imageView,requestRunnable);
	}

	/**
	 * 是否需要加载, 如果两次加载同样的url, 并且加载时间间隔很小, 则放弃第二次加载
	 *
	 * @param imageView
	 * @param lastImageUrl
	 * @param imageUrl
	 * @return
	 */
	private boolean isNeedLoad(ImageView imageView,String lastImageUrl,String imageUrl)
	{

		Object lastLoadTime = imageView.getTag(KEY_TAG_IMAGE_LOAD_TIME);
		long currentTime = System.currentTimeMillis();
		imageView.setTag(KEY_TAG_IMAGE_LOAD_TIME,Long.valueOf(currentTime));
		// 1, 第一次加载, 直接加载
		if(lastLoadTime == null || !(lastLoadTime instanceof Long))
		{
			return true;
		}

		// 2, 加载的连接不一样,重新加载
		if(!imageUrl.equals(lastImageUrl))
		{
			return true;
		}
		// 3, 两次相同加载的间隔, 不能太小
		long lastTime = ((Long)lastLoadTime).longValue();
		return (currentTime - lastTime) > MIN_LOAD_SAME_TIME;
	}

	/**
	 * <B>ImageView加载图片, {@link ImageView#setImageBitmap(Bitmap)}<B>
	 *
	 * @param imageUrl       网络图片地址;
	 * @param imageView      显示图片的ImageView<BR/>
	 *                       占用 {@link ImageView#setTag(int,Object)}, key为 {@link #KEY_TAG_IMAGE_URL};
	 * @param loadingImageId 加载过程中的默认图片ID.
	 * @param loadListener   {@link OnLoadCompleteListener}
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-8-15
	 */
	public void loadImageFromSD(final boolean shouldCompress,
			final String imageUrl,final ImageView imageView,
			final int loadingImageId,final int reqwidth,final int reqHeight,
			final OnLoadCompleteListener loadListener)
	{
		// 如果< ImageView >为空,或者< 图片地址 >为空,直接返回.
		if(imageView == null || TextUtils.isEmpty(imageUrl))
		{
			return;
		}

		// 1,从内存中获取
		String lastImageUrl = putImageUrlToTag(imageView,imageUrl);
		Bitmap bitmap = getBitmapFromMemoryCache(lastImageUrl,imageUrl);
		if(bitmap != null && !bitmap.isRecycled())
		{
			if(loadListener != null)
			{
				// listener不为空,说明调用者想自己处理加载结果
				loadListener.onLoadComplete(bitmap,imageView,imageUrl,
						new ImageLoadedParams(ImageLoadedParams.DURATION_MEMORY));
			}
			else
			{
				// 否则,显示图片
				imageView.setImageDrawable(new BasicDrawable(imageView.getResources(),bitmap));
			}
			return;
		}

		// 2,如果内存获取失败,则从SD卡中获取
		if(loadingImageId > 0)
		{
			// 先显示默认加载的图片.
			imageView.setImageResource(loadingImageId);
		}

		// 2.1 如果线程挂掉了,其实就是人为的调用清空缓存那个操作,说明不需要加载了,直接返回null
		if(mRequestExecutor.isShutdown())
		{
			// 不用图片了,也无须使用回调
			return;
		}

		// (3)从SD卡中获取图片
		Runnable requestRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				ImageLoaderResult result = null;

				// 2.3 从SD卡中获取
				Bitmap bitmap = null;
				// 如果需要压缩图片,走图片压缩;否则不压缩
				if(shouldCompress)
				{
					bitmap = getBitmapFromDiskCache(imageUrl);
				}
				else
				{
					bitmap = getBitmapFromDiskCache(imageUrl,reqwidth,reqHeight);
				}
				result = new ImageLoaderResult(bitmap,
						new ImageLoadedParams(ImageLoadedParams.DURATION_SDCARD));

				// 2.7 将Bitmap对象添加到内存缓存当中
				if(result.bitmap != null)
				{
					addBitmapToMemoryCache(imageUrl,result.bitmap);
				}

				// 2.8 判断图片是否发生改变
				// 如果加载好的图片地址,和<ImageView>加载的地址不同,
				// 说明这个<ImageView>又开启了别的图片加载,取消显示当前的图片.
				if(isImageUrlChanged(imageView,imageUrl))
				{
					return;
				}

				// 2.9 显示图片
				mHandler.postLoaderResult(imageUrl,imageView,loadListener,result);
			}
		};
		executeImageLoadTask(imageView,requestRunnable);
	}

	/**
	 * 加载四级页图片
	 *
	 * @param imageUrl       网络图片地址;
	 * @param imageView      显示图片的ImageView<BR/>
	 *                       占用 {@link ImageView#setTag(int,Object)}, key为 {@link #KEY_TAG_IMAGE_URL};
	 * @param loadingImageId 加载过程中的默认图片ID.
	 * @param loadListener   {@link OnLoadCompleteListener}
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-8-15
	 */
	public void loadGoodsDetailImage(final String imageUrl,final ImageView imageView,
			final int loadingImageId,final OnLoadCompleteListener loadListener)
	{

		// 如果< ImageView >为空,或者< 图片地址 >为空,直接返回.
		if(imageView == null || TextUtils.isEmpty(imageUrl))
		{
			return;
		}
		putImageUrlToTag(imageView,imageUrl);

		// 1,从内存中获取
		String lastImageUrl = putImageUrlToTag(imageView,imageUrl);
		Bitmap bitmap = getBitmapFromMemoryCache(lastImageUrl,imageUrl);
		if(bitmap != null && !bitmap.isRecycled())
		{
			if(loadListener != null)
			{
				// listener不为空,说明调用者想自己处理加载结果
				loadListener.onLoadComplete(bitmap,imageView,imageUrl,
						new ImageLoadedParams(ImageLoadedParams.DURATION_MEMORY));
			}
			else
			{
				// 否则,显示图片
				imageView.setImageDrawable(new BasicDrawable(imageView.getResources(),bitmap));
			}
			return;
		}

		// 2,如果内存获取失败,则从SD卡 or 网络中获取
		if(loadingImageId > 0)
		{
			// 先显示默认加载的图片.
			imageView.setImageResource(loadingImageId);
		}

		// 2.1 如果线程挂掉了,其实就是人为的调用清空缓存那个操作,说明不需要加载了,直接返回null
		if(mRequestExecutor.isShutdown())
		{
			// 不用图片了,也无须使用回调
			return;
		}

		// 2.2 开启线程,从SD卡 or 网络中获取
		Runnable requestRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				ImageLoaderResult result = null;
				ImageLoadedParams loadedParams = null;

				// 2.3 从SD卡中获取
				Bitmap bitmap = getBitmapFromDiskCache(imageUrl);

				// 2.4 如果SD中获取失败,则从网路中获取
				if(bitmap != null)
				{
					// 加载到内存中
					addBitmapToMemoryCache(imageUrl,bitmap);
					loadedParams = new ImageLoadedParams(ImageLoadedParams.DURATION_SDCARD);

					// 判断图片是否发生改变
					if(isImageUrlChanged(imageView,imageUrl))
					{
						return;
					}
					// 一旦SD加载结束, 无论是否成功, 回调结果
					result = new ImageLoaderResult(bitmap,loadedParams);
					mHandler.postLoaderResult(imageUrl,imageView,loadListener,result);
					return;
				}

				// 2.5 SD卡没有, 去网络中取
				result = loadNetImageAndSaveIfNeed(imageUrl);
				// 2.6 将Bitmap对象添加到内存缓存当中
				if(result.bitmap != null)
				{
					addBitmapToMemoryCache(imageUrl,result.bitmap);
				}
				// 2.7 判断图片是否发生改变
				// 如果加载好的图片地址,和<ImageView>加载的地址不同,
				// 说明这个<ImageView>又开启了别的图片加载,取消显示当前的图片.
				if(isImageUrlChanged(imageView,imageUrl))
				{
					return;
				}
				// 2.8 回调结果
				mHandler.postLoaderResult(imageUrl,imageView,loadListener,result);
			}
		};
		executeImageLoadTask(imageView,requestRunnable);
	}

	/**
	 * <B>ImageView加载图片, {@link ImageView#setImageBitmap(Bitmap)}<B>
	 *
	 * @param imageUrl     网络图片地址;
	 * @param loadListener 显示图片的ImageView <B>注意:</B>这里会占用{@link ImageView#setTag(int,Object)}方法;
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-8-15
	 */
	public void loadImage(final String imageUrl,final OnLoadCompleteListener loadListener)
	{
		// 如果< 图片地址 >为空,直接返回.
		if(loadListener == null || TextUtils.isEmpty(imageUrl))
		{
			return;
		}

		// 1,从内存中获取
		Bitmap bitmap = getBitmapFromMemoryCache(null,imageUrl);
		if(bitmap != null && !bitmap.isRecycled())
		{
			loadListener.onLoadComplete(bitmap,null,imageUrl,
					new ImageLoadedParams(ImageLoadedParams.DURATION_MEMORY));
			return;
		}

		// 2.1 如果线程挂掉了,其实就是人为的调用清空缓存那个操作,说明不需要加载了,直接返回null
		if(mRequestExecutor.isShutdown())
		{
			// 不用图片了,也无须使用回调
			return;
		}

		// 2.2 开启线程,从SD卡 or 网络中获取
		mRequestExecutor.execute(new Runnable()
		{
			@Override
			public void run()
			{
				ImageLoaderResult result = null;

				// 2.3 从SD卡中获取
				Bitmap bitmap = getBitmapFromDiskCache(imageUrl);

				// 2.4 如果SD中获取失败,则从网路中获取
				if(bitmap == null)
				{
					result = loadNetImageAndSaveIfNeed(imageUrl);
				}
				else
				{
					result = new ImageLoaderResult(bitmap,
							new ImageLoadedParams(ImageLoadedParams.DURATION_SDCARD));
				}

				// 2.5 将Bitmap对象添加到内存缓存当中
				if(result.bitmap != null)
				{
					addBitmapToMemoryCache(imageUrl,result.bitmap);
				}

				// 2.6 显示图片
				mHandler.postLoaderResult(imageUrl,null,loadListener,result);
			}
		});
	}

	/**
	 * 把图片地址放入ImageView中的Tag
	 *
	 * @param imageView ImageView
	 * @param imageUrl  图片链接
	 * @return 当前加载的图片链接
	 */
	private String putImageUrlToTag(ImageView imageView,String imageUrl)
	{
		Object currentUrl = imageView.getTag(KEY_TAG_IMAGE_URL);
		imageView.setTag(KEY_TAG_IMAGE_URL,imageUrl);
		if(currentUrl != null && currentUrl instanceof String)
		{
			return currentUrl.toString();
		}
		return "";
	}

	/**
	 * ImageView加载的图片是否发生了变化
	 *
	 * @param imageView ImageView
	 * @param imageUrl  图片链接
	 * @return true, 加载的图片发生了变化, false, 没有变化
	 */
	private boolean isImageUrlChanged(ImageView imageView,String imageUrl)
	{
		// 判断图片是否发生改变
		Object currentUrl = imageView.getTag(KEY_TAG_IMAGE_URL);
		// 如果加载好的图片地址,和<ImageView>加载的地址不同,
		return currentUrl != null && !currentUrl.equals(imageUrl);
	}

	/**
	 * 把图片加载线程放入ImageView中的Tag
	 *
	 * @param imageView ImageView
	 * @param loadTask  加载线程
	 */
	private void executeImageLoadTask(ImageView imageView,Runnable loadTask)
	{
		// 1, 如果有正在加载图片的线程, 结束掉
		Object lastTask = imageView.getTag(KEY_TAG_IMAGE_POOL);
		if(lastTask != null && lastTask instanceof Runnable)
		{
			if(mRequestExecutor.remove((Runnable)lastTask))
			{
				LogUtils.d(TAG,"cancel last load task .");
			}
		}
		// 2, 放入新的图片加载线程
		imageView.setTag(KEY_TAG_IMAGE_POOL,loadTask);
		mRequestExecutor.execute(loadTask);
	}

	/**
	 * 加载网络图片 & 需要保存并保存
	 *
	 * @param imageUrl 图片地址
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-12-30
	 */
	private ImageLoaderResult loadNetImageAndSaveIfNeed(String imageUrl)
	{
		int responseCode = -1;
		String errorMessage = "";
		long loadDuration = ImageLoadedParams.DURATION_ERROR;
		Bitmap bitmap = null;
		InputStream inputStream = null;
		try
		{
			// 构造成URL
			URL url = createURL(supportWebp(imageUrl));

			// 记录图片加载开始时间戳,用于图片加载耗时统计
			long loadStartTLS = System.currentTimeMillis();

			// 获取Connection
			HttpURLConnection connection = createHttpURLConnection(url);
			connection.setDoInput(true);
			connection.connect();

			// 返回码判断
			responseCode = connection.getResponseCode();
			if(responseCode != 200)
			{
				LogUtils.e(TAG,imageUrl + ", responseCode : " + responseCode);
				return new ImageLoaderResult(null,
						new ImageLoadedParams(loadDuration,responseCode,errorMessage));
			}
			// 获得响应流
			inputStream = connection.getInputStream();

			String contentType = connection.getHeaderField("Content-Type");
			if(Build.VERSION.SDK_INT <= 17 && isWebpBitmap(contentType))
			{
				LogUtils.d(TAG,"[image/webp] : " + imageUrl);
				// 获得数据数组
				byte[] data = getWebpBitmapByte(inputStream);
				// 计算出图片加载耗时
				loadDuration = System.currentTimeMillis() - loadStartTLS;
				// 以前是data → bitmap → file,这样导致了本地保存的图片文件非常的大,是因为bitmap保存文件的实现问题
				// 现在改成(1)data → file, (2)data → bitmap,保证了本地文件大小等同于实际网络文件大小
				// (1)保存图片到本地,异步操作,保证图片的及时显示
				saveImageInSDCard(data,imageUrl);
				// (2)构造bitmap
				bitmap = WebpUtils.webpToBitmap(data);
			}
			else
			{
				// 获得数据数组
				byte[] data = getBitmapByte(inputStream);
				// 计算出图片加载耗时
				loadDuration = System.currentTimeMillis() - loadStartTLS;
				// 以前是data → bitmap → file,这样导致了本地保存的图片文件非常的大,是因为bitmap保存文件的实现问题
				// 现在改成(1)data → file, (2)data → bitmap,保证了本地文件大小等同于实际网络文件大小
				// (1)保存图片到本地,异步操作,保证图片的及时显示
				saveImageInSDCard(data,imageUrl);
				// (2)构造bitmap
				bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
			}

		}
		catch(IOException e)
		{
			LogUtils.e(TAG,"<load fail>" + imageUrl);
			LogUtils.w(TAG,e);
		}
		catch(Exception e)
		{
			LogUtils.e(TAG,e);
		}
		catch(OutOfMemoryError e)
		{
			LogUtils.e(TAG,e);
			System.gc();
		}
		catch(NoClassDefFoundError e)
		{
			LogUtils.e(TAG,e);
		}
		finally
		{
			try
			{
				if(inputStream != null)
				{
					inputStream.close();
				}
			}
			catch(IOException e)
			{
				LogUtils.w(TAG,e);
			}
		}
		// (3)处理加载结果
		return new ImageLoaderResult(bitmap,
				new ImageLoadedParams(loadDuration,responseCode,errorMessage));
	}

	/**
	 * @param contentType 图片的返回类型, Content-Type 为 image/webp
	 * @return 是否是webp格式的图片
	 */
	private boolean isWebpBitmap(String contentType)
	{
		return "image/webp".equals(contentType);
	}

	/**
	 * InputStream to Byte[]
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private byte[] getBitmapByte(InputStream inputStream) throws IOException
	{
		// 构造输出流,
		// 不使用BitmapFactory.decodeStream(inputStream)方法,2.2及以下时有一个bug;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8196];
		int len = 0;
		while((len = inputStream.read(buffer)) != -1)
		{
			baos.write(buffer,0,len);
		}
		baos.flush();
		baos.close();

		return baos.toByteArray();
	}

	/**
	 * InputStream to Byte[]
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private byte[] getWebpBitmapByte(InputStream inputStream) throws IOException
	{
		return WebpUtils.streamToBytes(inputStream);
	}

	/**
	 * @param urlStr url串
	 * @return URL
	 * @throws MalformedURLException
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-31
	 */
	private URL createURL(String urlStr) throws MalformedURLException
	{
		return new URL(urlStr);
	}

	/**
	 * @param imageUrl 图片地址
	 * @return 支持webp的图片地址
	 */
	private String supportWebp(String imageUrl)
	{
		if(TextUtils.isEmpty(imageUrl))
		{
			return "";
		}
		int pos = imageUrl.indexOf('?');
		if(pos == -1)
		{
			return imageUrl + "?from=mobile";
		}
		// www.suning.com/image/test.jpg?param=123
		// 获取参数 param=123
		String paramStr = imageUrl.substring(pos + 1);
		if(paramStr.contains("from=mobile"))
		{
			return imageUrl;
		}
		// 获取前缀 www.suning.com/image/test.jpg?
		String preUrl = imageUrl.substring(0,pos + 1);
		if(paramStr.length() == 0)
		{
			return preUrl + "from=mobile";
		}
		// 得到最终连接 www.suning.com/image/test.jpg?  from=mobile&  param=123
		return preUrl + "from=mobile&" + paramStr;
	}

	/**
	 * @param url URL
	 * @return HttpURLConnection
	 * @throws IOException
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-31
	 */
	private HttpURLConnection createHttpURLConnection(URL url) throws IOException
	{
		return mOkUrlFactory.open(url);
	}

	/**
	 * 保存图片到SD卡中
	 *
	 * @param data     图片数据
	 * @param imageUrl 图片下载地址
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-3-25
	 */
	private void saveImageInSDCard(final byte[] data,final String imageUrl)
	{
		// 如果不支持SD卡,则不做任何操作
		if(mCacheType == CacheType.ONLY_MEMORY)
		{
			return;
		}
		if(mSaveExecutor.isShutdown())
		{
			return;
		}
		//另起线程:用于图片下载线程快速完成,及时返回图片,并且让线程池迅速执行其他图片请求
		mSaveExecutor.execute(new Runnable()
		{
			@Override
			public void run()
			{
				mDiskLruCache.saveImage(data,imageUrl);
			}
		});
	}

	/**
	 * 从LruCache中获取一张图片, 如果不存在就返回null.
	 *
	 * @param lastImageUrl 上次图片加载地址
	 * @param imageUrl     图片的URL地址.
	 * @return 对应传入键的Bitmap对象, 或者null.
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-31
	 */
	public Bitmap getBitmapFromMemoryCache(String lastImageUrl,String imageUrl)
	{
		if(!TextUtils.isEmpty(imageUrl) && mCacheType != CacheType.ONLY_SDCARD)
		{
			// 如果没有加载过图片,走加载流程
			if(TextUtils.isEmpty(lastImageUrl))
			{
				return mMemoryCache.getImage(imageUrl);
			}
			// 否则走重新加载流程
			return mMemoryCache.reGetImage(lastImageUrl,imageUrl);
		}
		return null;
	}

	/**
	 * 将一张图片存储到LruCache中.
	 *
	 * @param imageUrl 图片的URL地址.
	 * @param bitmap   LruCache的值,这里传入从网络上下载的Bitmap对象.
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-31
	 */
	public void addBitmapToMemoryCache(String imageUrl,Bitmap bitmap)
	{
		if(!TextUtils.isEmpty(imageUrl) && mCacheType != CacheType.ONLY_SDCARD && bitmap != null)
		{
			mMemoryCache.putImage(imageUrl,bitmap);
		}
	}

	/**
	 * 从内存中移除,如果存在,这个图片将会被recycle.
	 *
	 * @param imageUrl
	 * @author 12075179
	 */
	public void removeFromMemory(String imageUrl)
	{
		if(!TextUtils.isEmpty(imageUrl) && mCacheType != CacheType.ONLY_SDCARD)
		{
			mMemoryCache.recycle(imageUrl);
		}
	}

	/**
	 * 从DiskCache中获取一张图片, 如果不存在就返回null.
	 *
	 * @param imageUrl 图片的URL地址.
	 * @return 对应传入键的Bitmap对象, 或者null.
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-8-31
	 */
	public Bitmap getBitmapFromDiskCache(String imageUrl)
	{
		// 如果不支持SD卡获取,直接返回null
		if(mCacheType == CacheType.ONLY_MEMORY)
		{
			return null;
		}
		return mDiskLruCache.getImage(imageUrl);
	}

	/**
	 * 从文件中获取图片
	 *
	 * @param imageUrl 图片的URL地址.
	 * @param width    宽度
	 * @param height   高度
	 * @return 图片
	 * @Author 12075179
	 * @Date 2015-11-3
	 */
	public Bitmap getBitmapFromDiskCache(String imageUrl,int width,int height)
	{

		// 如果不支持SD卡获取,直接返回null
		if(mCacheType == CacheType.ONLY_MEMORY)
		{
			return null;
		}
		return mDiskLruCache.getImage(imageUrl,width,height);
	}

	/**
	 * 摧毁缓存,释放资源.
	 *
	 * @Description:
	 * @Author 12075179
	 * @Date 2014-8-15
	 */
	public void destory()
	{
		// 关闭线程池
		mRequestExecutor.shutdown();
		mSaveExecutor.shutdown();
		// 清除内存缓存
		mMemoryCache.destory();
	}

	/**
	 * 消息Handler
	 *
	 * @Title:
	 * @Description:
	 * @Author:12075179
	 * @Since:2015-8-31
	 * @Version:
	 */
	private static class LoadHandler extends Handler
	{
		/**
		 * ImageLoader弱引用
		 **/
		WeakReference<ImageLoader> mmLoaderReference;

		/**
		 * 构造方法
		 *
		 * @param loader
		 */
		public LoadHandler(ImageLoader loader)
		{
			mmLoaderReference = new WeakReference<ImageLoader>(loader);
		}

		/**
		 * 提交加载结果到UI线程
		 *
		 * @param imageUrl     图片地址
		 * @param imageView    图片View
		 * @param loadListener 加载监听
		 * @param loadResult   加载结果
		 * @Description:
		 * @Author 12075179
		 * @Date 2015-8-31
		 */
		public void postLoaderResult(final String imageUrl,final ImageView imageView,
				final OnLoadCompleteListener loadListener,final ImageLoaderResult loadResult)
		{
			ImageLoader loader = mmLoaderReference.get();
			if(loader == null)
			{
				return;
			}
			// 提交到UI线程显示图片
			post(new Runnable()
			{
				@Override
				public void run()
				{

					if(loadListener != null)
					{
						// listener不为空,说明调用者想自己处理加载结果
						loadListener.onLoadComplete(loadResult.bitmap,imageView,imageUrl,
								loadResult.loadedParams);
					}
					else if(imageView != null && loadResult.bitmap != null && !loadResult.bitmap
							.isRecycled())
					{
						// 否则,加载成功就显示图片
						LogUtils.d(TAG,"net or SD hit , show bitmap: " + imageUrl);
						//						imageView.setImageBitmap(loadResult.bitmap);
						Drawable drawable = new BasicDrawable(imageView.getResources(),
								loadResult.bitmap);
						//                        if (imageView.getDrawable() != null) {
						//                            TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{imageView.getDrawable(), drawable});
						//                            transitionDrawable.setCrossFadeEnabled(true);
						//                            transitionDrawable.startTransition(DRAWABLE_TRANSITION);
						//                            imageView.setImageDrawable(transitionDrawable);
						//                        } else {
						//                            imageView.setImageDrawable(drawable);
						//                        }
						imageView.setImageDrawable(drawable);
					}
				}
			});
		}
	}

	/**
	 * 加载完成监听器
	 *
	 * @Title:
	 * @Description:
	 * @Author:12075179
	 * @Since:2014-12-29
	 * @Version:
	 */
	public interface OnLoadCompleteListener
	{
		/**
		 * 加载图片完成的时候
		 *
		 * @param bitmap       加载完成的图片,如果<加载成功>,不为null, 否则为null.
		 * @param view         被加载的对象,View
		 * @param url          加载图片的地址
		 * @param loadedParams {@link ImageLoadedParams};<BR>
		 * @Description:
		 * @Author 12075179
		 * @Date 2014-12-29
		 */
		void onLoadComplete(Bitmap bitmap,View view,String url,ImageLoadedParams loadedParams);
	}
}
