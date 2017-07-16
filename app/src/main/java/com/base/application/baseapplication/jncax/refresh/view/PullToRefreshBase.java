package com.base.application.baseapplication.jncax.refresh.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.base.application.baseapplication.jncax.refresh.iml.IPullToRefresh;

/**
 * 实现上拉加载和下拉刷新的功能
 * Created by JerryCaicos on 2017/7/15 17:51.
 */

public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements
		IPullToRefresh<T>
{
	public PullToRefreshBase(Context context,
			@Nullable AttributeSet attrs)
	{
		super(context,attrs);
	}

	/**
	 * 定义了下拉刷新和上拉加载更多的接口
	 */
	public interface OnRefreshListener<V extends View>
	{
		/**
		 * 下拉松手后调用
		 */
		void onPullDownToRefresh(final PullToRefreshBase<V> refreshBaseView);

		/**
		 * 加载更多时被调用或者上拉时会被调用
		 */
		void onPullUpToLoading(final PullToRefreshBase<V> refreshBaseView);
	}
}
