package com.base.application.baseapplication.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.base.application.baseapplication.utils.BasicImageUtil;
import com.base.application.baseapplication.utils.LogUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by adminchen on 16/4/13 17:36.
 */
public class BasicDiskCache
{
	/**
	 * LOG TAG
	 **/
	private static final String TAG = "BasicDiskCache";

	/**
	 * 缓存目录名称
	 **/
	private static final String CACHE_DIR_NAME = "basic_ebuy";
	/**
	 * 每个图片需要缓存文件的数量(即: 一个图片在缓存中,通过几个文件来存储)
	 **/
	private static final int FILE_NUMS_PER_IMAGE = 1;
	/**
	 * 最大缓存大小(B)
	 **/
	private static final long MAX_CAHCE_SIZE = 10 * 1024 * 1024;

	/**
	 * SD卡缓存
	 **/
	private DiskLruCache mDiskLruCache;

	/**
	 * 全局单例
	 **/
	private static BasicDiskCache sInstance;

	/**
	 * @return 全局单例
	 */
	public static synchronized BasicDiskCache getInstance(Context context)
	{
		if(sInstance == null)
		{
			sInstance = new BasicDiskCache(context);
		}
		return sInstance;
	}

	/**
	 * 私有默认构造
	 */
	private BasicDiskCache(Context context)
	{
		// 初始化DiskLruCache
		File cacheDir = getDiskCacheDir(context,CACHE_DIR_NAME);
		if(cacheDir != null)
		{
			// 如果目录不存在,则创建
			if(!cacheDir.exists())
			{
				if(!cacheDir.mkdirs())
				{
					LogUtils.e(TAG,"[NO DISK CACHE] create cache dir fail.");
				}
			}
			// 创建DiskLruCache实例，初始化缓存数据
			try
			{
				mDiskLruCache = DiskLruCache.open(cacheDir,getAppVersion(context),
						FILE_NUMS_PER_IMAGE,MAX_CAHCE_SIZE);
			}
			catch(IOException e)
			{
				LogUtils.e(TAG,"[NO DISK CACHE]");
				LogUtils.e(TAG,e);
				mDiskLruCache = null;
			}
			LogUtils.i(TAG,"create disk cache success");
		}
		else
		{
			LogUtils.e(TAG,"[NO DISK CACHE] get system cache dir fail.");
		}
	}

	/**
	 * 保存图片
	 *
	 * @param data     图片数据
	 * @param imageUrl 图片链接
	 */
	public void saveImage(final byte[] data,final String imageUrl)
	{
		if(mDiskLruCache == null)
		{
			LogUtils.e(TAG,"[NO DISK CACHE] save image fail : " + imageUrl);
			return;
		}
		// 保存图片
		try
		{
			String key = getImageName(imageUrl);
			DiskLruCache.Editor editor = mDiskLruCache.edit(key);
			if(editor != null)
			{
				// 这里把网络中的图片下载后,同步放入SD卡中
				OutputStream outputStream = editor.newOutputStream(0);
				outputStream.write(data);
				editor.commit();
			}
		}
		catch(FileNotFoundException e)
		{
			LogUtils.e(TAG,e);
		}
		catch(IllegalStateException e)
		{
			LogUtils.e(TAG,e);
		}
		catch(IOException e)
		{
			LogUtils.e(TAG,e);
		}
		catch(OutOfMemoryError error)
		{
			LogUtils.e(TAG,error);
		}
	}

	/**
	 * 获取图片
	 *
	 * @param imageUrl 图片链接
	 * @return 图片
	 */
	public Bitmap getImage(String imageUrl)
	{
		if(mDiskLruCache == null)
		{
			LogUtils.e(TAG,"[NO DISK CACHE] get image fail : " + imageUrl);
			return null;
		}
		// 获取图片
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		DiskLruCache.Snapshot snapShot = null;
		Bitmap bitmap = null;
		try
		{
			String key = getImageName(imageUrl);
			snapShot = mDiskLruCache.get(key);
			if(snapShot != null)
			{
				fileInputStream = (FileInputStream)snapShot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
				if(fileDescriptor != null)
				{
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}
				else
				{
					bitmap = BitmapFactory.decodeStream(fileInputStream);
				}
			}
		}
		catch(IOException e)
		{
			LogUtils.e(TAG,e);
		}
		catch(OutOfMemoryError e)
		{
			LogUtils.e(TAG,e);
		}
		finally
		{
			if(fileInputStream != null)
			{
				try
				{
					fileInputStream.close();
				}
				catch(IOException e)
				{
					LogUtils.w(TAG,e);
				}
			}
		}
		return bitmap;
	}

	/**
	 * 获取图片
	 *
	 * @param imageUrl 图片链接
	 * @param width    图片输出宽度
	 * @param height   图片输出高度
	 * @return 图片
	 */
	public Bitmap getImage(String imageUrl,int width,int height)
	{
		if(mDiskLruCache == null)
		{
			LogUtils.e(TAG,"[NO DISK CACHE] get size image fail : " + imageUrl);
			return null;
		}
		// 获取图片
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		DiskLruCache.Snapshot snapShot = null;
		Bitmap bitmap = null;
		try
		{
			String key = getImageName(imageUrl);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			snapShot = mDiskLruCache.get(key);
			if(snapShot != null)
			{
				fileInputStream = (FileInputStream)snapShot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
				if(fileDescriptor != null)
				{
					BitmapFactory.decodeFileDescriptor(fileDescriptor,null,opts);
				}
				else
				{
					BitmapFactory.decodeStream(fileInputStream,null,opts);
				}
				// 关闭流
				if(fileInputStream != null)
				{
					fileInputStream.close();
					fileInputStream = null;
				}
			}
			// 改变一下opts
			BasicImageUtil.computeSampleSize(opts,width,height);
			snapShot = mDiskLruCache.get(key);
			if(snapShot != null)
			{
				fileInputStream = (FileInputStream)snapShot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
				if(fileDescriptor != null)
				{
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,null,opts);
				}
				else
				{
					bitmap = BitmapFactory.decodeStream(fileInputStream,null,opts);
				}
			}
		}
		catch(IOException e)
		{
			LogUtils.e(TAG,e);
		}
		catch(OutOfMemoryError e)
		{
			LogUtils.e(TAG,e);
		}
		finally
		{
			if(fileInputStream != null)
			{
				try
				{
					fileInputStream.close();
				}
				catch(IOException e)
				{
					LogUtils.w(TAG,e);
				}
			}
		}
		return bitmap;
	}


	/**
	 * 根据请求URL获取图片的名称
	 *
	 * @param url
	 * @return
	 */
	private static String getImageName(String url)
	{
		String[] array = url.trim().split("/");

		String name;
		int len = array.length;
		if(len > 4)
		{
			// 如果不是极限情况: http://www.suning.com/image/1.jpg 拆分为
			// 【http:】【】【www.suning.com】【image】【1.jpg】
			// 最终名称为: www.suning.com_image_1.jpg
			name = new StringBuffer(array[len - 3]).append("_")
												   .append(array[len - 2])
												   .append("_")
												   .append(array[len - 1])
												   .toString();
		}
		else if(len > 3)
		{
			// 极限情况: http://www.suning.com/1.jpg 拆分为
			// 【http:】【】【www.suning.com】【1.jpg】
			// 最终名称为: www.suning.com_1.jpg
			name = new StringBuffer(array[len - 2]).append("_")
												   .append(array[len - 1])
												   .toString();
		}
		else
		{
			// 这种情况就是非法URL
			name = url;
			LogUtils.e(TAG,"illegal url:" + url);
		}

		name = name.toLowerCase(Locale.ENGLISH);

		int suffixPos = name.indexOf("?");
		if(suffixPos > 0)
		{
			// 头像地址带有?,所以需要获取,否则头像一直使用同一个
			String suffix = name.substring(suffixPos + 1);
			name = new StringBuffer(suffix).append("_")
										   .append(name.substring(0,suffixPos)).toString();
		}

		int pos = -1;
		if(name.contains(".jpg"))
		{
			pos = name.indexOf(".jpg");
		}
		else if(name.contains(".png"))
		{
			pos = name.indexOf(".png");
		}
		else
		{
			LogUtils.w(TAG,"illegal image name:" + name);
		}

		if(pos > 0)
		{
			name = name.substring(0,pos);
		}

		// 异常字符串处理
		if(name.contains(" "))
		{
			name = name.replaceAll(" ","");
		}
		if(name.contains("\n"))
		{
			name = name.replaceAll("\n","");
		}
		if(name.contains("\r"))
		{
			name = name.replaceAll("\r","");
		}
		return name;
	}

	/**
	 * @param context      context
	 * @param cacheDirName 缓存目录名称
	 * @return 缓存目录
	 */
	private static File getDiskCacheDir(Context context,String cacheDirName)
	{
		File cacheDir;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable())
		{
			// 如果SD卡可用,则用外部缓存
			cacheDir = context.getExternalCacheDir();
		}
		else
		{
			// 否则使用内部缓存
			cacheDir = context.getCacheDir();
		}
		if(cacheDir == null)
		{
			// 内外部的缓存不一定总是可以获取到
			// 空间不足,或者被移除等状态,会导致获取不到
			LogUtils.e("getDiskCacheDir","get disk cache dir fail.");
			return null;
		}
		return new File(cacheDir,cacheDirName);
	}

	/**
	 * @param context {@link Context}
	 * @return 应用版本号
	 */
	private static int getAppVersion(Context context)
	{
		try
		{
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
					0);
			return info.versionCode;
		}
		catch(PackageManager.NameNotFoundException e)
		{
			LogUtils.e(TAG,e);
		}
		return 1;
	}

	/**
	 * 删除缓存文件
	 */
	public final void deleteCacheFile() {
		if (mDiskLruCache != null) {
			try {
				mDiskLruCache.delete();
			} catch (IOException e) {
				LogUtils.e("deleteCacheFile", e);
			}
		}
	}
}
