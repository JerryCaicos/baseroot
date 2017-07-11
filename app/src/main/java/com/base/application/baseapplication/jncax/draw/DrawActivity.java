package com.base.application.baseapplication.jncax.draw;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.base.application.baseapplication.R;

/**
 * Created by JerryCaicos on 16/3/2 11:27.
 */
public class DrawActivity extends Activity
{
	LinearLayout parentLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acjn_activity_draw);

		parentLayout = (LinearLayout)findViewById(R.id.my_layout_draw);

		addDrawView();
	}

	private void addDrawView()
	{
		MyDrawView view = new MyDrawView(this);
		view.setMinimumHeight(500);
		view.setMinimumWidth(300);
		view.invalidate();
		parentLayout.addView(view);
	}
}
