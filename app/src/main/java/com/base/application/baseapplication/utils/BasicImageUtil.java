package com.base.application.baseapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 图片工具类
 *
 * @Title:
 * @Description:
 * @Version:
 */
public class BasicImageUtil
{
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength,int maxNumOfPixels)
	{
		int initialSize = computeInitialSampleSize(options,minSideLength,maxNumOfPixels);
		int roundedSize;
		if(initialSize <= 8)
		{
			roundedSize = 1;
			while(roundedSize < initialSize)
			{
				roundedSize <<= 1;
			}
		}
		else
		{
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength,int maxNumOfPixels)
	{
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1)
						 ?1:(int)Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1)
						 ?128:(int)Math.min(Math.floor(w / minSideLength),Math.floor(
				h / minSideLength));
		if(upperBound < lowerBound)
		{
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if((maxNumOfPixels == -1) && (minSideLength == -1))
		{
			return 1;
		}
		else if(minSideLength == -1)
		{
			return lowerBound;
		}
		else
		{
			return upperBound;
		}
	}

	/**
	 * bitmap 对象转成byte数组
	 *
	 * @param bitmap
	 * @return
	 */
	public static byte[] convertBmpToByteArray(Bitmap.CompressFormat format,Bitmap bitmap)
	{
		byte[] data = null;
		if(!bitmap.isRecycled())
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(format,100,baos);
			data = baos.toByteArray();
		}
		return data;
	}

	/**
	 * 将sdcard中图片转换为bitmap对象
	 *
	 * @param imgFilePath
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap getBitmapFromSd(String imgFilePath)
	{
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;
		bfOptions.inTempStorage = new byte[12 * 1024];
		bfOptions.inInputShareable = true;
		// bfOptions.inJustDecodeBounds = true;
		File file = new File(imgFilePath);
		FileInputStream fs = null;
		Bitmap bmp = null;
		try
		{
			fs = new FileInputStream(file);
			// bmp = BitmapFactory.decodeStream(fs, null, bfOptions);
			// 12100128-add-减少内存使用
			bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(),null,bfOptions);
		}
		catch(FileNotFoundException e)
		{
			LogUtils.e("getBitmapFromSd",e);
		}
		catch(IOException e)
		{
			LogUtils.e("getBitmapFromSd",e);
		}
		finally
		{
			if(fs != null)
			{
				try
				{
					fs.close();
				}
				catch(IOException e)
				{
					LogUtils.e("getBitmapFromSd",e);
				}
			}
		}
		return bmp;
	}
}
