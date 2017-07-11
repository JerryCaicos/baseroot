package com.base.application.baseapplication.service;

import android.content.Context;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public interface BasicService
{
	/**
	 * 设备信息服务
	 **/
	String DEVICE_INFO = "device_info";
	/**
	 * 位置服务
	 **/
	String LOCATION = "location";
	/**
	 * 网络连接服务
	 **/
	String NET_CONNECT = "net_connect";

	/**
	 * 创建的时候
	 *
	 * @param context {@link Context}
	 * @Description:
	 */
	void onApplicationCreate(Context context);

	/**
	 * 销毁的时候
	 *
	 * @param context {@link Context}
	 * @Description:
	 */
	void onApplicationDestory(Context context);
}

