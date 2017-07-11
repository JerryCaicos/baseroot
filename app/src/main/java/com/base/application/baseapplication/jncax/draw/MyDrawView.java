package com.base.application.baseapplication.jncax.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by JerryCaicos on 16/3/2 11:31.
 */
public class MyDrawView extends View
{

	public MyDrawView(Context context)
	{
		super(context);
	}

	private int radius;

	private float width = 150;

	private float height = 80;

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
//		paint.setTextSize(40);

//		canvas.drawText("draw cicle:",20,100,paint);
//		canvas.drawCircle(120,100,10,paint);// 小圆

		RectF rectF = new RectF(0,0,1200,80);
		canvas.drawRoundRect(rectF,10,10,paint);

	}
}
