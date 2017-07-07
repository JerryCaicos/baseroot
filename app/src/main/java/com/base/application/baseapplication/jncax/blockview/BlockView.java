package com.base.application.baseapplication.jncax.blockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;


import com.base.application.baseapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adminchen on 16/1/20 14:24.
 */
public class BlockView extends RelativeLayout
{
	/**
	 * 水平间距
	 */
	private int mHorizontalSpacing;
	/**
	 * 垂直间距
	 */
	private int mVerticalSpacing;
	/**
	 * 宽度
	 */
	private int mWidth;
	/**
	 * 宽度2
	 */
	private int mWidthInXml;
	/**
	 * 是否自动两边对齐
	 */
	private boolean mAutoMatchBlockWidth;
	/**
	 * 最大显示行数
	 */
	private int mMaxLine = 999;
	/**
	 * 当前绘制行数
	 */
	private int mCurrentLine = 0;
	/**
	 * 第一次布局完成标志
	 */
	public boolean mHasLayoutedFlag = false;
	/**
	 * item点击事件监听器
	 */
	private OnBlockItemClickListener mItemClickListener;
	/**
	 * item长按事件监听
	 */
	private OnBlockItemLongClickListener mItemLongClickListener;

	/**
	 * @Title:BlockView
	 * @Description:标签列表视图控件<BR>（PS：不支持父布局的左右间距设置以及使用layout_margin设置间距，需要使用layout_marginXXX，否则BlockView会显示异常，设计时需要尽量避免）
	 * @Version:
	 */
	public BlockView(Context context,AttributeSet attrs)
	{
		super(context,attrs);
		// 初始化,获取参数配置
		initAttrs(context,attrs);
		// 初始化宽度
		initWidthInXml(context,attrs);
	}

	/**
	 * @Description:设置水平间距
	 */
	public void setHorizontalSpacing(int horizontalSpacing)
	{
		mHorizontalSpacing = horizontalSpacing;
	}

	/**
	 * @Description:设置垂直间距
	 */
	public void setVerticalSpacing(int verticalSpacing)
	{
		mVerticalSpacing = verticalSpacing;
	}

	/**
	 * @param listener item点击事件监听器
	 * @Description:设置标签列表视图的item点击事件
	 */
	public void setOnBlockItemClickListener(OnBlockItemClickListener listener)
	{
		mItemClickListener = listener;
	}

	/**
	 * @Description:获取行数
	 */
	public int getLineCount()
	{
		return mCurrentLine + 1;
	}

	/**
	 * @param maxLine 显示的最大行数
	 * @Description:设置显示的最大行数
	 */
	public void setMaxLine(int maxLine)
	{
		mMaxLine = maxLine;
	}

	/**
	 * @param adapter 数据适配器
	 * @Description:设置标签列表视图的数据适配器
	 */
	public void setAdapter(final BaseAdapter adapter)
	{
		// 使用宽度2
		mWidth = mWidthInXml;
		// 清理BlockView，并创建item集合
		SparseArray<List<View>> lineMap = clearAndCreateLineMap();
		if(adapter == null || adapter.getCount() == 0)
		{
			return;
		}
		// 设置数据，直接测量并添加item
		measureAndAddItems(adapter,lineMap);
	}

	/**
	 * @param adapter 数据适配器
	 * @Description:设置标签列表视图的数据适配器
	 */
	public void setAdapterAndWidth(BaseAdapter adapter,int width)
	{
		// 使用宽度2
		mWidth = mWidthInXml;
		if(width > 0)
		{
			// EBuyViManager.getManager().viWidth(this, width);
			getLayoutParams().width = width;
			mWidth = width;
		}
		// 清理BlockView，并创建item集合
		SparseArray<List<View>> lineMap = clearAndCreateLineMap();
		if(adapter == null || adapter.getCount() == 0)
		{
			return;
		}
		// 第二次之后设置数据，直接测量并添加item
		measureAndAddItems(adapter,lineMap);
	}

	/**
	 * @Description:初始化,获取参数配置
	 */
	private void initAttrs(Context context,AttributeSet attrs)
	{
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.BlockView);
		mHorizontalSpacing = array.getDimensionPixelSize(
				R.styleable.BlockView_horizontalSpacing,0);
		mVerticalSpacing = array.getDimensionPixelSize(
				R.styleable.BlockView_verticalSpacing,0);
		mAutoMatchBlockWidth = array.getBoolean(
				R.styleable.BlockView_autoMatchBlockWidth,false);
		array.recycle();
	}

	/**
	 * @Description:初始化宽度
	 */
	private void initWidthInXml(Context context,AttributeSet attrs)
	{
		int[] attrsArray = new int[]{android.R.attr.id, // 0
				android.R.attr.background, // 1
				android.R.attr.layout_width, // 2
				android.R.attr.layout_height, // 3
				android.R.attr.layout_marginLeft, // 4
				android.R.attr.layout_marginRight // 5
		};
		TypedArray ta = context.obtainStyledAttributes(attrs,attrsArray);
		// 获取宽度参数，发生异常时获取屏幕宽度
		try
		{
			mWidthInXml = ta.getDimensionPixelSize(2,
					ViewGroup.LayoutParams.MATCH_PARENT);
		}
		catch(Exception e)
		{
			mWidthInXml = getResources().getDisplayMetrics().widthPixels;
		}
		// 实际宽度需要减去左右Margin
		// PS：当前控件不支持父布局的左右间距设置以及使用layout_margin设置间距，需要使用layout_marginXXX，否则BlockView会显示异常，设计时需要尽量避免
		mWidthInXml -= ta.getDimensionPixelSize(4,0)
				+ ta.getDimensionPixelSize(5,0);
		ta.recycle();
	}

	/**
	 * @Description:清理BlockView，并创建item集合
	 */
	private SparseArray<List<View>> clearAndCreateLineMap()
	{
		// 防止重复添加，先清空子item
		removeAllViews();
		// 初始行数为0
		mCurrentLine = 0;
		// 所有item的集合，注意是k-v结构，k就是当前行数（从0算起），v是当前一行的item集合
		final SparseArray<List<View>> lineMap = new SparseArray<List<View>>();
		// 初始添加第一行的item集合（k为0）
		lineMap.put(mCurrentLine,new ArrayList<View>());
		return lineMap;
	}

	/**
	 * @Description:测量并添加item
	 */
	private void measureAndAddItems(BaseAdapter adapter,
			SparseArray<List<View>> lineMap)
	{
		// 测量每一个item的尺寸
		measureItems(adapter,lineMap);
		// 添加item
		addItems(lineMap);
	}

	/**
	 * @Description:测量每一个item的尺寸
	 */
	private void measureItems(BaseAdapter adapter,
			SparseArray<List<View>> lineMap)
	{
		// 通过逐行绘制的方式添加所有的标签item，以下为绘制的标签item的x坐标，y坐标（从0算起）
		int x = 0, y = 0;
		// 依次测量每一个item的尺寸，并添加到集合
		for(int i = 0, size = adapter.getCount();i < size;i++)
		{
			// item视图
			View item = adapter.getView(i,null,null);

			// 设置序号
			item.setTag(i);
			// 设置点击事件
			item.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(mItemClickListener != null)
					{
						mItemClickListener.onBlockItemClick(v,
								(Integer)v.getTag());
					}
				}
			});
			item.setOnLongClickListener(new OnLongClickListener()
			{

				@Override
				public boolean onLongClick(View v)
				{
					if(mItemLongClickListener != null)
					{
						mItemLongClickListener.onBlockItemLongClick(v,
								(Integer)v.getTag());
					}
					return true;
				}
			});
			// 测量item
			item.measure(View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED),View.MeasureSpec
					.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
			// 获取宽度
			int itemWidth = item.getMeasuredWidth();
			// 获取高度
			int itemHeight = item.getMeasuredHeight();
			// 当前绘制标签的x坐标+标签宽度如果大于总宽度，说明需要换行了
			if(x + itemWidth > mWidth)
			{
				// x坐标回到起点
				x = 0;
				// y坐标到下一行位置，下移距离为标签高度+垂直间距
				y += itemHeight + mVerticalSpacing;
				// 行数自增
				mCurrentLine++;
				// 添加下一行的item集合
				lineMap.put(mCurrentLine,new ArrayList<View>());
			}
			// 控制当前item的间距
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			// 左间距
			lp.leftMargin = x;
			// 上间距
			lp.topMargin = y;
			// 设置间距
			item.setLayoutParams(lp);
			// 添加到集合
			lineMap.get(mCurrentLine).add(item);
			// x坐标到下一个位置，右移距离为标签宽度+水平间距
			x += itemWidth + mHorizontalSpacing;
			// 如果超过最大行数，终止绘制
			if(mCurrentLine >= mMaxLine)
			{
				lineMap.remove(mCurrentLine);
				mCurrentLine -= 1;
				break;
			}
		}
	}

	/**
	 * @Description:添加item
	 */
	private void addItems(SparseArray<List<View>> lineMap)
	{
		// 依次将item添加到BlockView，并且设置是否需要自动两边对齐
		for(int i = 0;i <= mCurrentLine;i++)
		{
			// 一行的item个数
			int size = lineMap.get(i).size();
			// 判断是否需要两边对齐
			if(mAutoMatchBlockWidth)
			{
				// 该行最后一个item
				View lastItem = lineMap.get(i).get(size - 1);
				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)lastItem
						.getLayoutParams();
				// item右端的坐标
				int right = lp.leftMargin + lastItem.getMeasuredWidth();
				// 计算右边的空白距离
				int emptyWidth = mWidth - right;
				// 计算每一个item应该增加的左右间距（padding）
				int padding = emptyWidth / (size * 2);
				// 增加间距后，需要叠加的marginLeft，初始为0
				int leftOffset = 0;
				// 依次添加item到控件上
				for(int j = 0;j < size;j++)
				{
					View item = lineMap.get(i).get(j);
					// 重新设置间距
					RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams)item
							.getLayoutParams();
					// 重新设置marginLeft
					lp2.leftMargin += leftOffset;
					// 下一次叠加的位移
					leftOffset = (j + 1) * 2 * padding;
					// 重新设置padding
					item.setPadding(item.getPaddingLeft() + padding,
							item.getPaddingTop(),item.getPaddingRight()
									+ padding,item.getPaddingBottom());
					// 添加item到控件上
					addView(item);
				}
			}
			else
			{
				// 不需要两边对齐的情况，直接依次添加item到控件上
				for(int j = 0;j < size;j++)
				{
					addView(lineMap.get(i).get(j));
				}
			}
		}
	}

	/**
	 * @Title:OnBlockItemClickListener
	 * @Description:BlockView的点击事件监听器
	 * @Version:
	 */
	public interface OnBlockItemClickListener
	{
		void onBlockItemClick(View view,int position);
	}

	/**
	 * @Title:OnBlockLongItemClickListener
	 * @Description:长按的事件监听
	 * @Version:
	 */
	public interface OnBlockItemLongClickListener
	{
		void onBlockItemLongClick(View v,int position);
	}

	/**
	 * @Description:设置标签列表视图的item长按点击事件
	 */
	public void setOnBlockLongItemClickListener(
			OnBlockItemLongClickListener listener)
	{
		mItemLongClickListener = listener;
	}
}
