package com.base.application.baseapplication.jncax.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.base.application.baseapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JerryCaicos on 16/6/24 11:11.
 */
public class LayoutAnimationsActivity extends Activity
{
	/**
	 * LayoutAnimationsController可以用于实现使多个空间按顺序一个一个的显示。
	 * 1、LayoutAnimationsController用于为一个layout里的控件或者是ViewGroup里的控件设置统一的动画效果
	 * 2、每一个控件都有相同的动画效果，
	 * 3、控件的动画效果可以在不同的时间显示出来
	 * 4、LayoutAnimationsController可以在代码里设置，也可以在xml里设置
	 */

	private ListView listView;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acjn_activity_layoutanimation);

		listView = (ListView)findViewById(R.id.list_view);
		button = (Button)findViewById(R.id.ceshi);

		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				createListAdapter();
			}
		});
	}

	private void createListAdapter()
	{
		List<HashMap<String,String>> list = new ArrayList<>();
		HashMap<String,String> map1 = new HashMap<>();
		map1.put("name","sdfsdfsfs");
		map1.put("sex","sdfsdfsfs");
		HashMap<String,String> map2 = new HashMap<>();
		map2.put("name","wrew");
		map2.put("sex","fdfs");
		HashMap<String,String> map3 = new HashMap<>();
		map3.put("name","sfsfewf");
		map3.put("sex","353453");
		HashMap<String,String> map4 = new HashMap<>();
		map4.put("name","5756767");
		map4.put("sex","3454645");
		HashMap<String,String> map5 = new HashMap<>();
		map5.put("name","755675");
		map5.put("sex","453535");
		HashMap<String,String> map6 = new HashMap<>();
		map6.put("name","234334");
		map6.put("sex","45465");
		HashMap<String,String> map7 = new HashMap<>();
		map7.put("name","3456465");
		map7.put("sex","3455464");
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		list.add(map6);
		list.add(map7);
		SimpleAdapter simpleAdapter = new SimpleAdapter(this,list,
				R.layout.acjn_layoutanimation_item_layout,
				new String[]{"name","sex"},new int[]{R.id.item_name,R.id.item_sex});
		listView.setAdapter(simpleAdapter);

		//创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
		AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
		//设置动画执行的时间
		alphaAnimation.setDuration(1000);

		LayoutAnimationController layoutAnimationController = new LayoutAnimationController(
				alphaAnimation);
		layoutAnimationController.setDelay(0.5f);
		layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
		listView.setLayoutAnimation(layoutAnimationController);
	}
}
