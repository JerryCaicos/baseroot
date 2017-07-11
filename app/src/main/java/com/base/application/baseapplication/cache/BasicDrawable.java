package com.base.application.baseapplication.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by JerryCaicos on 16/6/23 22:13.
 */
public class BasicDrawable extends BitmapDrawable
{

	/**
	 * Create drawable from a bitmap, setting initial target density based on
	 * the display metrics of the resources.
	 */
	public BasicDrawable(Resources res,Bitmap bitmap)
	{
		super(res,bitmap);
	}

	@Override
	public void draw(Canvas canvas)
	{
		final Bitmap bitmap = getBitmap();
		if(bitmap == null || bitmap.isRecycled())
		{
			return;
		}
		super.draw(canvas);
	}
}