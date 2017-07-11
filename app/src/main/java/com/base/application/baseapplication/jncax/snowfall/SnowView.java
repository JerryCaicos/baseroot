package com.base.application.baseapplication.jncax.snowfall;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by JerryCaicos on 16/4/22 10:27.
 */
public class SnowView extends View
{

	public static final int NUM_SNOWFLAKES = 150;

	public static final int DELAY = 5;

	private SnowFlake[] flakes;


	public SnowView(Context context)
	{
		super(context);
	}

	public SnowView(Context context,AttributeSet attrs)
	{
		super(context,attrs);
	}

	public SnowView(Context context,AttributeSet attrs,int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

	private void resize(int width,int height)
	{
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
	}

}
