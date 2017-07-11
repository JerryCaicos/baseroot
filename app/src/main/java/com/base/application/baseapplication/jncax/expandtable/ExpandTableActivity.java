package com.base.application.baseapplication.jncax.expandtable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.base.application.baseapplication.R;
import com.base.application.baseapplication.jncax.expandtable.adapter.ExpandGridAdapter;
import com.base.application.baseapplication.jncax.expandtable.adapter.MyViewPagerAdapter;
import com.base.application.baseapplication.jncax.expandtable.custom.CurrenPositionView;
import com.base.application.baseapplication.jncax.expandtable.custom.MyTextView;
import com.base.application.baseapplication.jncax.expandtable.model.BaseData;
import com.base.application.baseapplication.jncax.expandtable.model.ZhaoPin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryCaicos on 16/1/29 11:14.
 */
public class ExpandTableActivity extends Activity implements ExpandGridAdapter.OnClick
{

	private List<ZhaoPin> data = null;
	private LinearLayout currenPositionLinear = null;
	private Integer duration = 200;
	private RelativeLayout rel;
	private String itemId = null;
	private String clickPosition = "";
	private MyTextView clicktxt;
	private LinearLayout circlelayout;
	private int lastlocation = -1;
	private ExpandGridAdapter.OnClick listener;
	private final int NUM_LINE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acjn_activity_expand_table);
		initData();
		initView();
	}

	private void initView()
	{
		currenPositionLinear = (LinearLayout)findViewById(R.id.zp_curr_potion_linear);
		for(int num = 0;num < data.size();num++)
		{
			ZhaoPin zhaoPin = data.get(num);
			CurrenPositionView currenPositionView = new CurrenPositionView(this);
			currenPositionView.setData(zhaoPin);
			currenPositionView.setViewId(num);
			currenPositionView.init();
			if(num > 0)
			{
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.topMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,
						getResources().getDisplayMetrics());
				if(num == data.size() - 1)
				{
					params.bottomMargin = (int)TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics());
				}
				currenPositionLinear.addView(currenPositionView,params);
			}
			else
			{
				currenPositionLinear.addView(currenPositionView);
			}

		}
	}

	private void initData()
	{
		if(listener == null)
		{
			listener = this;
		}
		if(data == null)
		{
			data = Utils.getJobType(ExpandTableActivity.this);
		}
	}

	public class OnItemClick implements View.OnClickListener, ViewPager.OnPageChangeListener
	{
		private int position;
		private int tag;
		private MyTextView view;
		private TableLayout tableLayout;
		private ZhaoPin zhaoPin;

		/**
		 * @param zhaoPin 数据模型
		 * @param position 数据模型在数据集中的位置
		 * @param tag 行数
		 * @param table 显示子列表的table
		 * @param view 显示子列表item的view
		 */
		public OnItemClick(ZhaoPin zhaoPin,int position,int tag,TableLayout table,MyTextView view)
		{
			this.position = position;
			this.tag = tag;
			this.view = view;
			this.tableLayout = table;
			this.zhaoPin = zhaoPin;
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{

		}

		@Override
		public void onPageScrolled(int arg0,float arg1,int arg2)
		{
			if(lastlocation != arg0 && lastlocation != -1)
			{
				ImageView mimage = (ImageView)circlelayout.findViewWithTag(lastlocation + 1 + 10);
				if(mimage != null)
				{
					mimage.setImageResource(R.drawable.black_circle);
				}
			}

			ImageView image = (ImageView)circlelayout.findViewWithTag(arg0 + 1 + 10);
			if(image != null)
			{
				image.setImageResource(R.drawable.acjn_orange_circle);
			}

			lastlocation = arg0;
		}

		@Override
		public void onPageSelected(int arg0)
		{

		}

		private void init(View v,int location)
		{
			List<BaseData> list = new ArrayList<BaseData>();
			ViewPager pager = (ViewPager)v.findViewById(R.id.expand_item);
			circlelayout = (LinearLayout)v.findViewById(R.id.circle_layout);
			circlelayout.removeAllViews();
			int radio = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,
					getResources().getDisplayMetrics());
			LinearLayout.LayoutParams circleparams = new LinearLayout.LayoutParams(radio,radio);
			circleparams.setMargins(0,0,10,0);
			List<View> views = new ArrayList<View>();
			int row = zhaoPin.getJobtype().get(location).getJobtype().size() / NUM_LINE;
			if(row > 4)
			{
				row = 4;
			}
			else if(row < 4)
			{
				if(zhaoPin.getJobtype().get(location).getJobtype().size() % NUM_LINE != 0)
				{
					row += 1;
				}
			}
			int i = zhaoPin.getJobtype().get(location).getJobtype().size() / 12;
			int len = zhaoPin.getJobtype().get(location).getJobtype().size() % 12;
			if(len > 0)
			{
				i += 1;
			}
			for(int n = 1;n <= i;n++)
			{
				View view = LayoutInflater.from(ExpandTableActivity.this).inflate(
						R.layout.acjn_viewpager,null);
				GridView grid = (GridView)view.findViewById(R.id.expand_grid);
				if(i == 0)
				{
					list = zhaoPin.getJobtype().get(location).getJobtype();
				}
				else if(n < i)
				{
					list = zhaoPin.getJobtype().get(location).getJobtype().subList((n - 1) * 12,
							n * 12);
				}
				else
				{
					int size = zhaoPin.getJobtype().get(location).getJobtype().size();
					list = zhaoPin.getJobtype().get(location).getJobtype().subList((n - 1) * 12,
							size);
				}
				ExpandGridAdapter ea = new ExpandGridAdapter(ExpandTableActivity.this,list,
						R.layout.acjn_expand_grid_item);
				ea.setOnClick(listener);
				grid.setAdapter(ea);
				views.add(grid);
				if(i > 1)
				{
					circlelayout.setVisibility(View.VISIBLE);
					ImageView image = new ImageView(ExpandTableActivity.this);
					image.setTag(n + 10);
					image.setImageResource(R.drawable.black_circle);
					circlelayout.addView(image,circleparams);
				}
				else
				{
					circlelayout.setVisibility(View.GONE);
				}
			}
			MyViewPagerAdapter mpa = new MyViewPagerAdapter(views);
			int height = (int)(getResources().getDisplayMetrics().density * 42 + 0.5f);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,row * height);
			pager.setLayoutParams(params);
			pager.setAdapter(mpa);
			pager.setOnPageChangeListener(this);
		}

		private void collapse(final View v)
		{
			final int initialHeight = v.getMeasuredHeight();
			Animation animation = new Animation()
			{
				@Override
				protected void applyTransformation(float interpolatedTime,
						Transformation t)
				{
					if(interpolatedTime == 1)
					{
						v.setVisibility(View.GONE);
					}
					else
					{
						v.getLayoutParams().height = initialHeight
								- (int)(initialHeight * interpolatedTime);
						v.requestLayout();
					}
				}

				@Override
				public boolean willChangeBounds()
				{
					return true;
				}
			};

			animation.setDuration(duration);
			v.startAnimation(animation);
		}

		private void expand(final View v,int location)
		{
			init(v,location);
			v.measure(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			final int targetHeight = v.getMeasuredHeight();
			v.getLayoutParams().height = 0;
			v.setVisibility(View.VISIBLE);

			Animation animation = new Animation()
			{
				@Override
				protected void applyTransformation(float interpolatedTime,
						Transformation t)
				{
					v.getLayoutParams().height = (interpolatedTime == 1)?RelativeLayout.LayoutParams.WRAP_CONTENT
																		:(int)(targetHeight * interpolatedTime);
					v.requestLayout();
				}

				@Override
				public boolean willChangeBounds()
				{
					return true;
				}
			};
			animation.setDuration(duration);
			v.startAnimation(animation);
		}

		@Override
		public void onClick(View v)
		{
			String tempTag = (String)v.getTag();
			if(rel == null)
			{
				rel = (RelativeLayout)tableLayout.findViewWithTag(tag);
				expand(rel,position);
				view.isDraw(true);
			}
			else
			{
				if(rel.getVisibility() == View.VISIBLE)
				{
					collapse(rel);
					view.isDraw(false);
				}
				else
				{
					if(tempTag.equals(clickPosition))
					{
						expand(rel,position);
					}
					view.isDraw(true);
				}

				if(!tempTag.equals(clickPosition))
				{
					rel = (RelativeLayout)tableLayout.findViewWithTag(tag);
					expand(rel,position);
					clicktxt.isDraw(false);
					view.isDraw(true);
				}
			}
			clickPosition = tempTag;
			clicktxt = view;
		}
	}

	@Override
	public void onClick(int dataId,String str)
	{
		Intent intent = new Intent();
		intent.putExtra("id",dataId);
		intent.putExtra("value",str);
		setResult(RESULT_OK,intent);
		finish();
	}

}