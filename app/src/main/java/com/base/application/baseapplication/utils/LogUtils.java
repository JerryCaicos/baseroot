package com.base.application.baseapplication.utils;

import android.util.Log;

/**
 * Created by JerryCaicos on 16/1/16.
 */

/**
 * 日志工具
 *
 * @Title:
 * @Description:
 * @Version:
 */
public class LogUtils
{

	/**
	 * 默认日志TAG.
	 **/
	private static final String TAG = "LogUtils";

	/**
	 * 日志开关
	 **/
	public static boolean logEnabled = true;

	/**
	 * @Description:
	 * @see Log#v(String,String)
	 */
	public static void v(String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.v(TAG,msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#v(String,String)
	 */
	public static void v(Object object,String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.v(getTag(object),msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#d(String,String)
	 */
	public static void d(String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.d(TAG,msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#d(String,String)
	 */
	public static void d(Object object,String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.d(getTag(object),msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#i(String,String)
	 */
	public static void i(String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.i(TAG,msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#i(String,String)
	 */
	public static void i(Object object,String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.i(getTag(object),msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#w(String,String)
	 */
	public static void w(String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.w(TAG,msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#w(String,String)
	 */
	public static void w(Object object,String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.w(getTag(object),msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#w(String,String,Throwable)
	 */
	public static void w(Object object,Throwable error)
	{
		if(logEnabled && error != null)
		{
			Log.w(getTag(object),"",filterThrowable(error));
		}
	}

	/**
	 * @Description:
	 * @see Log#e(String,String)
	 */
	public static void e(String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.e(TAG,msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#e(String,String)
	 */
	public static void e(Object object,String msg)
	{
		if(logEnabled && msg != null)
		{
			Log.e(getTag(object),msg);
		}
	}

	/**
	 * @Description:
	 * @see Log#e(String,String,Throwable)
	 */
	public static void e(Object object,Throwable error)
	{
		if(logEnabled && error != null)
		{
			Log.e(getTag(object),"",filterThrowable(error));
		}
	}

	/**
	 * @return 可定位的异常
	 * @Description:
	 */
	private static Throwable filterThrowable(Throwable error)
	{
		StackTraceElement[] ste = error.getStackTrace();
		error.setStackTrace(new StackTraceElement[]{ste[0]});
		return error;
	}

	/**
	 * @return Log TAG.
	 * @Description:
	 */
	private static String getTag(Object object)
	{
		if(object == null)
		{
			return TAG;
		}
		String name = object.getClass().getSimpleName();
		if("String".equals(name))
		{
			return object.toString();
		}
		return name;
	}
}

