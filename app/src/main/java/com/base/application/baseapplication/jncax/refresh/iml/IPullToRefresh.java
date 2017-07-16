package com.base.application.baseapplication.jncax.refresh.iml;

import android.view.View;

import com.base.application.baseapplication.jncax.refresh.view.LoadingLayout;
import com.base.application.baseapplication.jncax.refresh.view.PullToRefreshBase;

/**
 * 定义了拉动刷新的接口
 * Created by JerryCaicos on 2017/7/15 17:30.
 */

public interface IPullToRefresh<T extends View>
{
	/**
	 * 设置下拉刷新是否可用
	 *
	 * @param pullRefreshEnable true 可用，false 不可用
	 */
	public void setPullDownRefreshEnable(boolean pullRefreshEnable);

	/**
	 * 设置上拉加载更多是否可用
	 *
	 * @param upLoadingEnable true 可用，false 不可用
	 */
	public void setPullUpLoadingEnable(boolean upLoadingEnable);

	/**
	 * 设置滑动到底部是否自动加载更多
	 *
	 * @param bottomAutoLoadingEnable true 上拉加载更多禁用
	 */
	public void setScrollBottomAutoLoadingEnable(boolean bottomAutoLoadingEnable);

	/**
	 * 判断当前下拉刷新是否可用
	 *
	 * @return true 可用，false 不可用
	 */
	public boolean isPullDownRefreshEnable();

	/**
	 * 判断当前上拉加载更多是否可用
	 *
	 * @return true 可用，false 不可用
	 */
	public boolean isPullUpLoadingEnable();

	/**
	 * 判断滑动到底部加载更多是否可用
	 *
	 * @return true 可用，false 不可用
	 */
	public boolean isScrollBottomAutoLoadingEnable();

	/**
	 * 设置刷新的监听器
	 *
	 * @param refreshListener 监听对象
	 */
	public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> refreshListener);

	/**
	 * 下拉刷新完成
	 */
	public void onPullDownRefreshCompleted();

	/**
	 * 上拉加载更多完成
	 */
	public void onPullUpLoadingCompleted();

	/**
	 * 得到可刷新的view对象
	 *
	 * @return 返回调用{@link #createRefreshableView(Context, AttributeSet)} 方法返回的对象
	 */
	public T getRefreshableView();

	/**
	 * 获取下拉刷新Header Layout
	 *
	 * @return Header 布局对象
	 */
	public LoadingLayout getHeaderLoadingLayout();

	/**
	 * 获取上拉加载更多 Footer Layout
	 *
	 * @return Footer 布局对象
	 */
	public LoadingLayout getFooterLoadingLayout();

	/**
	 * 设置最近一次跟新的文本
	 *
	 * @param lastUpdatedLable 最近一次更新的文本
	 */
	public void setLastUpdatedLable(CharSequence lastUpdatedLable);


}
