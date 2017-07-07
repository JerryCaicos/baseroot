package com.base.application.baseapplication.cache;

import android.graphics.Bitmap;

/**
 * Created by adminchen on 16/4/13 17:41.
 */
public class ImageLoaderResult
{
	/**
	 * 下载好的图片
	 **/
	protected Bitmap bitmap;
	/**
	 * {@link ImageLoadedParams}
	 **/
	protected ImageLoadedParams loadedParams;

	/**
	 * 构造方法
	 *
	 * @param bitmap       图片
	 * @param loadedParams 加载参数
	 */
	protected ImageLoaderResult(Bitmap bitmap,ImageLoadedParams loadedParams)
	{
		this.bitmap = bitmap;
		this.loadedParams = loadedParams;
	}
}