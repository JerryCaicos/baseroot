package com.base.application.baseapplication.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.base.application.baseapplication.utils.LogUtils;

import java.util.LinkedHashMap;

/**
 * Created by adminchen on 16/4/13 17:35.
 */
public class BasicLruCache extends LruCache<String,Bitmap>
{

	/**
	 * LOG TAG
	 **/
	private static final String TAG = "BasicLruCache";

	/**
	 * 是否销毁
	 **/
	private boolean isDestory;

	/**
	 * 图片使用记录
	 **/
	private LinkedHashMap<String,Integer> hitMap;

	/**
	 * 构造方法
	 *
	 * @param maxSize 最大内存大小(KB)
	 */
	public BasicLruCache(int maxSize)
	{
		super(maxSize);
		this.hitMap = new LinkedHashMap<String,Integer>(0,0.75f,true);
	}

	/**
	 * 获取图片
	 *
	 * @param imageUrl 图片地址
	 * @return 图片
	 */
	public final Bitmap getImage(String imageUrl)
	{
		// 每次从内存中获取时,都将imageUrl对于的图片引用+1
		Integer hitNum = hitMap.get(imageUrl);
		if(hitNum == null)
		{
			hitNum = Integer.valueOf(1);
		}
		else
		{
			hitNum = Integer.valueOf(hitNum.intValue() + 1);
		}
		hitMap.put(imageUrl,hitNum);

		return get(imageUrl);
	}

	/**
	 * 从新获取图片
	 *
	 * @param lastImageUrl 上次加载的图片地址
	 * @param imageUrl     这次加载的图片地址
	 * @return 图片
	 */
	public final Bitmap reGetImage(String lastImageUrl,String imageUrl)
	{
		// 每次从内存中重新获取时,都将上次imageUrl对于的图片引用-1
		Integer hitNum = hitMap.get(lastImageUrl);
		if(hitNum != null)
		{
			hitNum = Integer.valueOf(hitNum.intValue() - 1);
		}
		hitMap.put(lastImageUrl,hitNum);

		// 然后重新获取新的图片
		return getImage(imageUrl);
	}

	/**
	 * 存放图片
	 *
	 * @param imageUrl 图片地址
	 * @param bitmap   图片
	 */
	public final void putImage(String imageUrl,Bitmap bitmap)
	{
		put(imageUrl,bitmap);
	}

	@Override
	protected void entryRemoved(boolean evicted,String key,Bitmap oldBitmap,Bitmap newBitmap)
	{

		// 如果属于替代, 则不管
		if(newBitmap != null)
		{
			return;
		}

		// 否则根据图片的计数器, 判断是否要进行释放
		if(oldBitmap != null && !oldBitmap.isRecycled())
		{
			Integer hitNum = hitMap.remove(key);
			// 如果图片没有被引用, 或者内存缓存不再使用(即销毁),释放图片
			if(hitNum == null || hitNum.intValue() < 1 || isDestory)
			{
				// 如果已经销毁,并且图片不为空,则释放图片资源
				oldBitmap.recycle();
				LogUtils.d(TAG,"recycle bitmap, [" + size() + "(B) left]");
			}
		}
	}

	@Override
	protected int sizeOf(String key,Bitmap bitmap)
	{
		if(bitmap != null)
		{
			return bitmap.getByteCount() / 1024;
		}
		return 0;
	}

	/**
	 * 释放图片.
	 *
	 * @param key
	 */
	protected final void recycle(String key)
	{
		final Bitmap bitmap = remove(key);
		if(bitmap != null && !bitmap.isRecycled())
		{
			bitmap.recycle();
			LogUtils.e(TAG,"recycle bitmap, [" + size() + "(B) left]");
		}
	}

	/**
	 * 销毁内存缓存
	 */
	protected void destory()
	{
		isDestory = true;
		// 释放内存缓存
		evictAll();
	}
}
