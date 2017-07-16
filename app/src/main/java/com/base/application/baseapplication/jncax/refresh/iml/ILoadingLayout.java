package com.base.application.baseapplication.jncax.refresh.iml;

/**
 * 下拉刷新和上拉加载更多的界面接口
 * Created by JerryCaicos on 2017/7/15 17:09.
 */

public interface ILoadingLayout
{
	/**
	 * 当前的状态
	 */
	public enum State
	{
		/**
		 * 初始状态
		 */
		NONE,
		/**
		 * When the UI is in a state witch means that user is not interacting
		 * with the Pull_To_Refresh function.
		 */
		RESET,
		/**
		 * When the UI is being pulled by the user, but has not been pulled for
		 * enough so that it refreshes when released.
		 */
		PULL_TO_REFRESH,
		/**
		 * When the UI is being pulled by the user, and has been pulled for enough
		 * so that it refresh when released.
		 */
		RELEASE_TO_REFRESH,
		/**
		 * When the UI currently refreshing, cause by a pulled gesture.
		 */
		REFRESHING,
		/**
		 * When the UI currently loading, caused by a pulled gesture.
		 */
		LOADING,
		/**
		 * No more data to load.
		 */
		NO_MORE_DATA,
	}

	/**
	 * 设置当前的状态，派生类应该根据这个状态的变化来改变View 的变化
	 */
	void setState(State state);

	/**
	 * 获取当前的状态
	 *
	 * @return State
	 */
	State getState();

	/**
	 * 获取当前layout的内容大小，它将作为一个刷新的临界点
	 *
	 * @return 高度
	 */
	int getContentSize();

	/**
	 * 在拉动时调用
	 *
	 * @param scale 拉动的比例
	 */
	void onPull(float scale);


}
