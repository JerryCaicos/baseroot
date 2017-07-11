package com.base.application.baseapplication.utils;


import com.base.application.baseapplication.BasicApplication;
import com.base.application.baseapplication.custom.toast.BasicToast;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public class ToastUtils
{
	/**
	 * 弹出Toast
	 *
	 * @param resId 信息的资源ID
	 * @Author 12075179
	 * @Date 2015-9-18
	 */
	public static void showMessage(final int resId)
	{
		BasicApplication application = BasicApplication.getInstance();
		BasicToast.create(application,application.getText(resId),BasicToast.Duration.SHORT)
				  .show();
	}

	/**
	 * 弹出Toast
	 *
	 * @param msg 信息字符串
	 * @Author 12075179
	 * @Date 2015-9-18
	 */
	public static void showMessage(CharSequence msg)
	{
		BasicToast.create(BasicApplication.getInstance(),msg,BasicToast.Duration.SHORT)
				  .show();
	}
}
