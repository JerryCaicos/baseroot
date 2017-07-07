package com.base.application.baseapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.base.application.baseapplication.utils.LogUtils;

/**
 * Created by adminchen on 16/1/16.
 */
public class NetConnectService extends BroadcastReceiver implements BasicService
{

	/**
	 * 没有网络,即: WIFI不能用,移动网络不能用
	 **/
	public static final int TYPE_NONE = 1;

	/**
	 * 正在使用移动网络
	 **/
	public static final int TYPE_MOBILE = 2;

	/**
	 * 正在使用无线网络
	 **/
	public static final int TYPE_WIFI = 3;

	/**
	 * 当前网络连接类型
	 **/
	private int networkType;

	/**
	 * <LI>{@link #TYPE_NONE} : WIFI不能用,移动网络不能用</LI>
	 * <LI>{@link #TYPE_MOBILE} : 正在使用移动网络</LI>
	 * <LI>{@link #TYPE_WIFI} : 正在使用无线网络</LI>
	 *
	 * @return 当前网络连接状态
	 */
	public int getNetworkType()
	{
		return networkType;
	}

	/**
	 * @return 网络是否可用(就是是否可以上网),true:可以; false:不可以.
	 */
	public boolean isNetworkAvailable()
	{
		return networkType != TYPE_NONE;
	}

	@Override
	public void onApplicationCreate(Context context)
	{

		// 更新下网络状态
		updateConnectState(context);

		//注册网络连接状态的广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(this,filter);
	}

	@Override
	public void onApplicationDestory(Context context)
	{
		//取消注册
		context.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context,Intent intent)
	{
		updateConnectState(context);
	}

	/**
	 * WIFI关闭, 移动数据关闭
	 * <UL>
	 * <UL><B>NetworkInfo:</B> null
	 * </UL>
	 * <UL>
	 * <B>WifiInfo:</B>
	 * <UL>
	 * <B>SSID:</B>             <none>, <BR>
	 * <B>BSSID:</B>            <none>, <BR>
	 * <B>MAC:</B>              90:c1:15:73:f7:9b, <BR>
	 * <B>Supplicant state:</B> COMPLETED, (也可能是SCANNING)<BR>
	 * <B>RSSI:</B>             -200, <BR>
	 * <B>Link speed:</B>       -1, <BR>
	 * <B>Net ID:</B>           -1<BR>
	 * <B>Explicit connect:</B> false<BR>
	 * </UL>
	 * </UL>
	 * </UL>
	 * WIFI关闭, 移动数据打开
	 * <UL>
	 * <UL><B>NetworkInfo:</B>
	 * <UL>
	 * <B>type:</B>        mobile[HSPA], <BR>
	 * <B>state:</B>       CONNECTED/CONNECTED, <BR>
	 * <B>reason:</B>      dataEnabled, <BR>
	 * <B>extra:</B>       3gnet, <BR>
	 * <B>roaming:</B>     false, <BR>
	 * <B>failover:</B>    true, <BR>
	 * <B>isAvailable:</B> true<BR>
	 * </UL>
	 * </UL>
	 * <UL>
	 * <B>WifiInfo:</B>
	 * <UL>
	 * <B>SSID:</B>             <none>, <BR>
	 * <B>BSSID:</B>            <none>, <BR>
	 * <B>MAC:</B>              90:c1:15:73:f7:9b, <BR>
	 * <B>Supplicant state:</B> COMPLETED, (也可能是SCANNING)<BR>
	 * <B>RSSI:</B>             -200, <BR>
	 * <B>Link speed:</B>       -1, <BR>
	 * <B>Net ID:</B>           -1<BR>
	 * <B>Explicit connect:</B> false<BR>
	 * </UL>
	 * </UL>
	 * </UL>
	 * WIFI打开,但无网络选择, 移动数据打开
	 * <UL>
	 * <UL><B>NetworkInfo:</B>
	 * <UL>
	 * <B>type:</B>        mobile[HSPA], <BR>
	 * <B>state:</B>       CONNECTED/CONNECTED, <BR>
	 * <B>reason:</B>      dataEnabled, <BR>
	 * <B>extra:</B>       3gnet, <BR>
	 * <B>roaming:</B>     false, <BR>
	 * <B>failover:</B>    true, <BR>
	 * <B>isAvailable:</B> true<BR>
	 * </UL>
	 * </UL>
	 * <UL>
	 * <B>WifiInfo:</B>
	 * <UL>
	 * <B>SSID:</B>             <none>, <BR>
	 * <B>BSSID:</B>            <none>, <BR>
	 * <B>MAC:</B>              90:c1:15:73:f7:9b, <BR>
	 * <B>Supplicant state:</B> SCANNING, <BR>
	 * <B>RSSI:</B>             -200, <BR>
	 * <B>Link speed:</B>       -1, <BR>
	 * <B>Net ID:</B>           -1<BR>
	 * <B>Explicit connect:</B> false<BR>
	 * </UL>
	 * </UL>
	 * </UL>
	 * WIFI打开,但无网络选择, 移动数据关闭
	 * <UL>
	 * <UL><B>NetworkInfo:</B> null
	 * </UL>
	 * <UL>
	 * <B>WifiInfo:</B>
	 * <UL>
	 * <B>SSID:</B>             <none>, <BR>
	 * <B>BSSID:</B>            <none>, <BR>
	 * <B>MAC:</B>              90:c1:15:73:f7:9b, <BR>
	 * <B>Supplicant state:</B> SCANNING, <BR>
	 * <B>RSSI:</B>             -200, <BR>
	 * <B>Link speed:</B>       -1, <BR>
	 * <B>Net ID:</B>           -1<BR>
	 * <B>Explicit connect:</B> false<BR>
	 * </UL>
	 * </UL>
	 * </UL>
	 * WIFI打开,已选择网络, 移动数据打开
	 * <UL>
	 * <UL><B>NetworkInfo:</B>
	 * <UL>
	 * <B>type:</B>        WIFI[], <BR>
	 * <B>state:</B>       CONNECTED/CONNECTED, <BR>
	 * <B>reason:</B>      (unspecified), <BR>
	 * <B>extra:</B>       (none), <BR>
	 * <B>roaming:</B>     false, <BR>
	 * <B>failover:</B>    false, <BR>
	 * <B>isAvailable:</B> true<BR>
	 * </UL>
	 * </UL>
	 * <UL>
	 * <B>WifiInfo:</B>
	 * <UL>
	 * <B>SSID:</B>             OFFICE-Adviser, <BR>
	 * <B>BSSID:</B>            24:de:c6:9a:43:53, <BR>
	 * <B>MAC:</B>              90:c1:15:73:f7:9b, <BR>
	 * <B>Supplicant state:</B> COMPLETED, <BR>
	 * <B>RSSI:</B>             -77, <BR>
	 * <B>Link speed:</B>       65, <BR>
	 * <B>Net ID:</B>           2<BR>
	 * <B>Explicit connect:</B> true<BR>
	 * </UL>
	 * </UL>
	 * </UL>
	 */
	private void updateConnectState(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null && info.isAvailable())
		{
			LogUtils.d(this,"NetworkInfo:" + info.toString());
			if(info.getType() == ConnectivityManager.TYPE_WIFI)
			{
				// WIFI网络
				networkType = TYPE_WIFI;
			}
			else
			{
				// 2G, 3G等
				networkType = TYPE_MOBILE;
			}
		}
		else
		{
			// 没有网络可使用
			networkType = TYPE_NONE;
		}
	}
}
