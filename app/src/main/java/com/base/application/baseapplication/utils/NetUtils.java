package com.base.application.baseapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public class NetUtils
{
	/**
	 * 没有网络
	 */
	public static final int NETWORKTYPE_INVALID = 0;
	/**
	 * wap网络
	 */
	public static final int NETWORKTYPE_WAP = 1;
	/**
	 * 2G网络
	 */
	public static final int NETWORKTYPE_2G = 2;
	/**
	 * 3G和3G以上网络，或统称为快速网络
	 */
	public static final int NETWORKTYPE_3G = 3;
	/**
	 * wifi网络
	 */
	public static final int NETWORKTYPE_WIFI = 4;
	/**
	 * url中的域名断点
	 */
	private static final char PERIOD = '.';
	private static final String SLASH = "/";
	private static final String COLON = ":";

	/**
	 * 获取网络状态，wifi,wap,2g,3g.
	 *
	 * @param context 上下文
	 * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},          *{@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}* <p>{@link #NETWORKTYPE_WIFI}
	 */

	public static int getNetWorkType(Context context)
	{

		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		int mNetWorkType = NETWORKTYPE_INVALID;
		if(networkInfo != null && networkInfo.isConnected())
		{
			String type = networkInfo.getTypeName();

			if(type.equalsIgnoreCase("WIFI"))
			{
				mNetWorkType = NETWORKTYPE_WIFI;
			}
			else if(type.equalsIgnoreCase("MOBILE"))
			{
				String proxyHost = android.net.Proxy.getDefaultHost();

				mNetWorkType = TextUtils.isEmpty(proxyHost)
							   ?(isFastMobileNetwork(context)?NETWORKTYPE_3G:NETWORKTYPE_2G)
							   :NETWORKTYPE_WAP;
			}
		}
		else
		{
			mNetWorkType = NETWORKTYPE_INVALID;
		}

		return mNetWorkType;
	}

	private static boolean isFastMobileNetwork(Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(
				Context.TELEPHONY_SERVICE);
		switch(telephonyManager.getNetworkType())
		{
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return false; // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return true; // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return true; // ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return false; // ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return true; // ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return true; // ~ 700-1700 kbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return true; // ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return true; // ~ 400-7000 kbps
			case TelephonyManager.NETWORK_TYPE_EHRPD:
				return true; // ~ 1-2 Mbps
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				return true; // ~ 5 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return true; // ~ 10-20 Mbps
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return false; // ~25 kbps
			case TelephonyManager.NETWORK_TYPE_LTE:
				return true; // ~ 10+ Mbps
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;
			default:
				return false;
		}
	}

	/**
	 * @Description:获取网络连接方式wifi,cmnet,cmwap
	 * @Author 13050527
	 * @Date 2013-6-14
	 */
	public static String getNetType(Context context)
	{
		String netType = null;
		if(null != context)
		{
			NetworkInfo info = null;
			try
			{
				ConnectivityManager conn = (ConnectivityManager)context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				info = conn != null?conn.getActiveNetworkInfo():null;
			}
			catch(Exception e)
			{
				info = null;
			}
			if(null != info && info.isAvailable() && info.isConnected())
			{
				String type = info.getTypeName().toLowerCase();// MOBILE（GPRS）;WIFI
				if("mobile".equals(type) || "mobile2".equals(type))
				{
					String apn = info.getExtraInfo();
					apn = apn != null?apn.toLowerCase():null;
					if("cmwap".equalsIgnoreCase(apn))
					{
						netType = "td-wap";
					}
					else if("3gwap".equalsIgnoreCase(apn)
							|| "uniwap".equalsIgnoreCase(apn))
					{
						netType = "w-wap";
					}
					else if("ctwap".equalsIgnoreCase(apn))
					{
						netType = "c-wap";
					}
					else
					{
						netType = "net";
					}
				}
				else if("wifi".equalsIgnoreCase(type))
				{
					netType = "wifi";
				}
				else
				{
					netType = "net";
				}
			}
		}
		return netType;
	}

	/**
	 * 获取是否有网络连接方法
	 *
	 * @param context
	 * @return
	 */
	public static NetworkInfo getActiveNetwork(Context context)
	{
		if(null != context)
		{
			ConnectivityManager mConnMgr = (ConnectivityManager)context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(null != mConnMgr)
			{
				NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo();
				// 获取活动网络连接信息
				return aActiveInfo;
			}
		}
		return null;
	}

	public static String getProxyStr(String netType)
	{
		String proxyStr = null;
		if("w-wap".equalsIgnoreCase(netType)
				|| "td-wap".equalsIgnoreCase(netType))
		{
			proxyStr = "10.0.0.172";
		}
		else if("c-wap".equalsIgnoreCase(netType))
		{
			proxyStr = "10.0.0.200";
		}
		return proxyStr;
	}

	public static boolean isIpAddress(final String host)
	{
		if(!TextUtils.isEmpty(host))
		{
			// 定义正则表达式
			String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
					+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
			// 判断ip地址是否与正则表达式匹配
			if(host.matches(regex))
			{
				// 返回判断信息
				return true;
			}
			else
			{
				// 返回判断信息
				return false;
			}
		}
		// 返回判断信息
		return false;
	}

	public static boolean isWifi(Context context)
	{
		boolean isWifi = false;
		if(null != context)
		{
			ConnectivityManager conn = (ConnectivityManager)context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = conn != null?conn.getActiveNetworkInfo()
										   :null;
			if(null != info && info.isAvailable() && info.isConnected())
			{
				String type = info.getTypeName().toLowerCase();// MOBILE（GPRS）;WIFI
				if("wifi".equals(type))
				{
					isWifi = true;
				}
			}
		}
		return isWifi;
	}
}

