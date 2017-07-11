package com.base.application.baseapplication.tabhost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.base.application.baseapplication.BasicActivity;
import com.base.application.baseapplication.BasicApplication;
import com.base.application.baseapplication.R;
import com.base.application.baseapplication.custom.header.HeaderBuilder;
import com.base.application.baseapplication.service.BasicService;
import com.base.application.baseapplication.service.DeviceInfoService;
import com.base.application.baseapplication.service.NetConnectService;
import com.base.application.baseapplication.utils.LogUtils;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public class BasicFragment extends Fragment
{
	protected final String TAG = this.getClass().getSimpleName();

	/**
	 * Header构建器
	 **/
	private HeaderBuilder mHeaderBuilder;


	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		LogUtils.i(TAG,"onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LogUtils.i(TAG,"onCreate");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		LogUtils.i(TAG,"onActivityCreated");
	}

	@Override
	public void onStart()
	{
		super.onStart();
		LogUtils.i(TAG,"onStart");
	}

	@Override
	public void onResume()
	{
		super.onResume();
		LogUtils.i(TAG,"onResume");
	}

	@Override
	public void onPause()
	{
		super.onPause();
		LogUtils.i(TAG,"onPause");
	}

	@Override
	public void onStop()
	{
		super.onStop();
		LogUtils.i(TAG,"onStop");
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		LogUtils.i(TAG,"onDestroyView");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		LogUtils.i(TAG,"onDestroy");
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		LogUtils.i(TAG,"onDetach");
	}

	/**
	 * @param view 需要添加标题的View
	 * @return 一个添加标题的View
	 * @Description:
	 */
	@SuppressLint("InflateParams")
	protected View createTitleContentView(View view)
	{
		// 获得带有标题的ContentView
		LinearLayout contentView = (LinearLayout)getActivity().getLayoutInflater().inflate(
				R.layout.activity_title_container,null);
		// 1,创建header,并添加
		mHeaderBuilder = new HeaderBuilder(getActivity());
		// 统一标题创建的时候, 重写这个方法可设置header
		onCreateHeader(mHeaderBuilder);
		// 把设置好的Header添加到ContentView中
		View headerView = mHeaderBuilder.getHeaderView();
		contentView.addView(headerView,new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				getResources().getDimensionPixelSize(R.dimen.public_dimen_fourty_eight)));

		// 2,把View添加到ContentView中
		contentView.addView(view,new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		return contentView;
	}

	/**
	 * 统一标题创建的时候, 重写这个方法可设置header<BR>
	 *
	 * @param header {@link HeaderBuilder}
	 */
	protected void onCreateHeader(HeaderBuilder header)
	{
		// 1.添加返回键点击事件
		header.setBackActionListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 调用事件
				onBackKeyPressed();
			}
		});
	}

	/*
	 * 当统一标题左边的返回按钮被点击,会调用这个方法.<BR>
	 *
	 * @Author 12075179
	 * @Date 2015-9-24
	 */
	protected void onBackKeyPressed()
	{
	}

	/**
	 * 设置返回按钮的点击事件
	 *
	 * @param listener 返回按钮的点击事件
	 */
	public void setHeaderBackClickListener(View.OnClickListener listener)
	{
		if(mHeaderBuilder != null)
		{
			mHeaderBuilder.setBackActionListener(listener);
		}
	}

	/**
	 * 设置返回按钮的可见性
	 *
	 * @param visible true: 显示返回键, false: 隐藏返回键.
	 */
	public void setHeaderBackVisible(boolean visible)
	{
		if(mHeaderBuilder != null)
		{
			int visibility = visible?View.VISIBLE:View.GONE;
			mHeaderBuilder.setBackActionVisibility(visibility);
		}
	}

	/**
	 * 设置header标题
	 *
	 * @param titleResId 标题资源ID
	 */
	public void setHeaderTitle(int titleResId)
	{
		setHeaderTitle(getText(titleResId));
	}

	/**
	 * 设置header标题
	 *
	 * @param title 标题字符串
	 */
	public void setHeaderTitle(CharSequence title)
	{
		if(mHeaderBuilder != null)
		{
			mHeaderBuilder.setTitle(title);
		}
	}

	/**
	 * 设置标题的可见性
	 *
	 * @param visible true: 显示标题, false: 隐藏标题.
	 */
	public void setHeaderTitleVisible(boolean visible)
	{
		if(mHeaderBuilder != null)
		{
			int visibility = visible?View.VISIBLE:View.GONE;
			mHeaderBuilder.setTitleVisibility(visibility);
		}
	}


	/**
	 * 显示加载框
	 *
	 * @Description:
	 */
	public void showLoadingView()
	{
		// 显示对话框,避免在onSaveInstanceState(outState)之后展示
		if(!isResumed())
		{
			return;
		}
		BasicActivity activity = getBasicActivity();
		if(activity != null)
		{
			// TODO: 16/1/16
			//			activity.showLoadingView();
		}
	}

	/**
	 * 隐藏加载框
	 *
	 * @Description:
	 */
	public void hideLoadingView()
	{
		BasicActivity activity = getBasicActivity();
		if(activity != null)
		{
			// TODO: 16/1/16
			//			activity.hideLoadingView();
		}
	}

	/**
	 * @return BasicActivity
	 * @Description:
	 */
	public final BasicActivity getBasicActivity()
	{
		Activity activity = getActivity();
		if(activity instanceof BasicActivity)
		{
			return (BasicActivity)activity;
		}
		return null;
	}


	/**
	 * @param service 服务名称
	 * @Description:
	 */
	public BasicService getService(String service)
	{
		return BasicApplication.getInstance().getService(service);
	}


	/**
	 * @return 返回“设备信息服务”{@link DeviceInfoService}
	 */
	public DeviceInfoService getDeviceInfoService()
	{
		return (DeviceInfoService)getService(BasicService.DEVICE_INFO);
	}


	/**
	 * @return 屏幕宽度
	 */
	public int getScreenWidth()
	{
		return getDeviceInfoService().screenWidth;
	}

	/**
	 * @return 屏幕高度
	 */
	public int getScreenHeight()
	{
		return getDeviceInfoService().screenHeight;
	}


	/**
	 * @return 是否有网络
	 */
	public boolean isNetworkAvailable()
	{
		NetConnectService netService = (NetConnectService)getService(BasicService.NET_CONNECT);
		return netService.isNetworkAvailable();
	}

	/**
	 * 弹出信息对话框
	 *
	 * @param msgId 信息资源ID
	 */
	public void displayAlertMessag(int msgId)
	{
		BasicActivity activity = getBasicActivity();
		if(activity != null)
		{
			// TODO: 16/1/16
			//			activity.displayAlertMessag(msgId);
		}
	}

	/**
	 * 弹出信息对话框
	 *
	 * @param msg 信息字符串
	 */
	public void displayAlertMessag(CharSequence msg)
	{
		BasicActivity activity = getBasicActivity();
		if(activity != null)
		{
			// TODO: 16/1/16
			//			activity.displayAlertMessag(msg);
		}
	}

	/**
	 * 弹出信息对话框
	 *
	 * @param msg     信息字符串
	 * @param btnText 按钮文字
	 */
	public void displayAlertMessag(CharSequence msg,CharSequence btnText)
	{
		BasicActivity activity = getBasicActivity();
		if(activity != null)
		{
			// TODO: 16/1/16
			//			activity.displayAlertMessag(msg,btnText);
		}
	}

	/**
	 * 弹出信息对话框
	 *
	 * @param msg              信息字符串
	 * @param btnText          按钮文字
	 * @param btnClickListener 按钮点击事件
	 */
	public void displayAlertMessag(CharSequence msg,CharSequence btnText,
			View.OnClickListener btnClickListener)
	{
		BasicActivity activity = getBasicActivity();
		if(activity != null)
		{
			// TODO: 16/1/16
			//			activity.displayAlertMessag(msg,btnText,btnClickListener);
		}
	}

	/**
	 * 展示对话框
	 *
	 * @param title              标题,隐藏标题View则传null or ""
	 * @param msg                信息,隐藏信息View则传null or ""
	 * @param leftBtnText        左边按钮的文字,传 null or "",默认显示"取消"
	 * @param leftClickListener  左边按钮的点击事件,可为null.
	 * @param rightBtnText       右边按钮的文字,传 null or "",默认显示"确定"
	 * @param rightClickListener 右边按钮的点击事件,可为null.
	 */
	public void displayDialog(CharSequence title,CharSequence msg,
			CharSequence leftBtnText,View.OnClickListener leftClickListener,
			CharSequence rightBtnText,View.OnClickListener rightClickListener)
	{
		BasicActivity activity = getBasicActivity();
		if(activity != null)
		{
			// TODO: 16/1/16
			//			activity.displayDialog(title,msg,leftBtnText,leftClickListener,rightBtnText,
			//				rightClickListener);
		}
	}

	/**
	 * 弹出信息提示
	 *
	 * @param msgId 信息资源ID
	 */
	public void displayToast(int msgId)
	{
		BasicActivity activity = getBasicActivity();
		if(activity != null)
		{
			activity.displayToast(msgId);
		}
	}

	/**
	 * 弹出信息提示
	 *
	 * @param msg 信息字符串
	 */
	public void displayToast(CharSequence msg)
	{
		BasicActivity activity = getBasicActivity();
		if(activity != null)
		{
			activity.displayToast(msg);
		}
	}
}
