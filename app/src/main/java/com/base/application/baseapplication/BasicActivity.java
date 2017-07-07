package com.base.application.baseapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.base.application.baseapplication.custom.dialog.BasicDialogFragment;
import com.base.application.baseapplication.custom.dialog.CustomDialog;
import com.base.application.baseapplication.custom.dialog.LoadingDialog;
import com.base.application.baseapplication.custom.header.HeaderBuilder;
import com.base.application.baseapplication.service.BasicService;
import com.base.application.baseapplication.service.DeviceInfoService;
import com.base.application.baseapplication.service.NetConnectService;
import com.base.application.baseapplication.utils.LogUtils;
import com.base.application.baseapplication.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class BasicActivity extends AppCompatActivity
{
	/**
	 * LOG TAG
	 **/
	protected final String TAG = this.getClass().getSimpleName();

	/**
	 * Header构建器
	 **/
	private HeaderBuilder mHeaderBuilder;

	/**
	 * 加载对话框
	 **/
	private LoadingDialog.Controller mLoadingController;

	/**
	 * 当Activity没有出resume或者pause状态时,弹出对话框则有问题, 这时需要保存这些对话框,然后在Activity恢复的时候展示.
	 */
	private List<BasicDialogFragment> mDialogList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		try
		{
			return super.dispatchTouchEvent(ev);
		}
		catch(Exception e)
		{
			LogUtils.e("dispatchTouchEvent exception",e.getMessage());
		}
		return false;
	}

	/**
	 * @param layoutResID 布局资源ID
	 * @param isShowTitle 是否展示标题;<BR>
	 *                    如果true,将把你的布局外面<B>加一层含有标题的布局</B>,并作为Activity的contentView.
	 * @Description:
	 * @see #setContentView(int)
	 */
	public void setContentView(int layoutResID,boolean isShowTitle)
	{
		if(isShowTitle)
		{
			View view = getLayoutInflater().inflate(layoutResID,null);
			setContentView(getTitleContentView(view));
		}
		else
		{
			setContentView(layoutResID);
		}
	}

	/**
	 * @param view 需要添加标题的View
	 * @return 一个添加标题的View
	 * @Description:
	 */
	@SuppressLint("InflateParams")
	private View getTitleContentView(View view)
	{
		// 获得带有标题的ContentView
		LinearLayout contentView = (LinearLayout)getLayoutInflater().inflate(
				R.layout.activity_title_container,null);
		// 1,创建header,并添加
		mHeaderBuilder = new HeaderBuilder(this);
		// 统一标题创建的时候, 重写这个方法可设置header
		onCreateHeader(mHeaderBuilder);
		// 把设置好的Header添加到ContentView中
		View headerView = mHeaderBuilder.getHeaderView();
		contentView.addView(headerView,new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,getResources()
				.getDimensionPixelSize(R.dimen.public_dimen_fourty_eight)));

		// 2,把View添加到ContentView中
		contentView.addView(view,new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		return contentView;
	}

	/**
	 * 统一标题创建的时候, 重写这个方法可设置header<BR>
	 * 如果在{@link #setContentView(int,boolean)}中,没有显示标题,则不会调用改方法.
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
				// 如果实现类处理完毕,则不继续操作
				if(onBackKeyPressed())
				{
					return;
				}
				// 否则结束当前Activity
				finish();
			}
		});
		// 2.设置卫星按钮
		// createSatelliteMenu();
		//		header.addActionView(mStatelliteMenu);
	}

	/**
	 * 创建卫星菜单
	 *
	 * @Author 12075179
	 * @Date 2016-1-14
	 */
	//	private View createSatelliteMenu()
	//	{
	//		// 获取ActionView
	//		View actionView = LayoutInflater.from(this).inflate(
	//				R.layout.view_header_action_view, null);
	//		ImageView icon = (ImageView) actionView
	//				.findViewById(R.id.iv_action_icon);
	//		icon.setImageResource(R.drawable.androidy);
	//		actionView.setOnClickListener(new View.OnClickListener()
	//		{
	//			@Override
	//			public void onClick(View v)
	//			{
	//				// 如果菜单Pop没有创建,则创建
	//				if(mSatelliteMenu == null)
	//				{
	//					mSatelliteMenu = new PopupMenu(SuningActivity.this);
	//					// 卫星菜单创建的时候,重写这个方法可设置卫星菜单
	//					onCreateSatelliteMenu(mSatelliteMenu);
	//				}
	//				// 如果有红点,让消息图标也有红点
	//				mSatelliteMenu.updateMenuItemRedMark(SMNU_ID_MSG,
	//						isSatelliteMenuMarkVisible());
	//				// 显示菜单
	//				mSatelliteMenu.show(v);
	//				// 数据统计
	//				StatisticsTools.setClickEvent("820501");
	//			}
	//		});
	//		// 卫星菜单的红点
	//		View redMark = actionView.findViewById(R.id.iv_action_mark);
	//		redMark.setVisibility(View.GONE);
	//
	//		return actionView;
	//	}

	/**
	 * 卫星菜单创建的时候,重写这个方法可设置卫星菜单<BR>
	 * 必须添加的卫星按钮之后,这个方法才会被调用.
	 *
	 * @param menu 卫星菜单
	 * @Author 12075179
	 * @Date 2015-9-21
	 */
	//	protected void onCreateSatelliteMenu(PopupMenu menu)
	//	{
	//		menu.setOnItemSelectedListener(new PopupMenu.OnItemSelectedListener()
	//		{
	//			@Override
	//			public void onItemSelected(
	//					com.suning.mobile.ebuy.base.host.custom.MenuItem item)
	//			{
	//				SuningIntent intent = new SuningIntent(SuningActivity.this);
	//				switch (item.getItemId())
	//				{
	//					case SMNU_ID_MSG:
	//						CustomServiceUtility
	//								.launcherNewsList(SuningActivity.this);
	//						// 去【消息中心】
	//						// 数据统计
	//						// StatisticsTools.setClickEvent("820502");
	//						break;
	//					case SMNU_ID_HOME:
	//						// 去【首页】
	//						intent.toHome();
	//						// 数据统计
	//						StatisticsTools.setClickEvent("820502");
	//						break;
	//					case SMNU_ID_SEARCH:
	//						// 去【搜索】
	//						intent.toSearch();
	//						// 数据统计
	//						StatisticsTools.setClickEvent("820503");
	//						break;
	//					case SMNU_ID_CATALORY:
	//						// 去【分类】
	//						intent.toCategory();
	//						// 数据统计
	//						StatisticsTools.setClickEvent("820504");
	//						break;
	//					case SMNU_ID_SHOPCART:
	//						// 去【购物车】
	//						intent.toShopcart();
	//						StatisticsTools.setClickEvent("820505");
	//						break;
	//					case SMNU_ID_MYEBUY:
	//						// 去【我的易购】
	//						intent.toMyEbuy();
	//						// 数据统计
	//						StatisticsTools.setClickEvent("820506");
	//						break;
	//					default:
	//						break;
	//				}
	//			}
	//		});
	//		menu.add(SMNU_ID_MSG, R.string.msg_center_tab).setIcon(
	//				getResources().getDrawable(R.drawable.msg_center_icon));
	//		menu.add(SMNU_ID_HOME, R.string.home_tab).setIcon(
	//				getResources().getDrawable(R.drawable.navi_home));
	//		menu.add(SMNU_ID_SEARCH, R.string.search_tab).setIcon(
	//				getResources().getDrawable(R.drawable.navi_search));
	//		menu.add(SMNU_ID_CATALORY, R.string.category_tab).setIcon(
	//				getResources().getDrawable(R.drawable.navi_cateloge));
	//		menu.add(SMNU_ID_SHOPCART, R.string.cart_tab).setIcon(
	//				getResources().getDrawable(R.drawable.navi_shopcart));
	//		menu.add(SMNU_ID_MYEBUY, R.string.my_ebuy_tab).setIcon(
	//				getResources().getDrawable(R.drawable.navi_myebuy));
	//	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		// 物理返回键按下的时候,也调用一下onBackKeyPressed.
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			if(onBackKeyPressed())
			{
				return true;
			}
		}
		return super.onKeyDown(keyCode,event);
	}

	@Override
	public void finish()
	{
		super.finish();
	}

	/**
	 * 当统一标题左边的返回按钮被点击,或者物理返回键被点击,都会调用这个方法.<BR>
	 *
	 * @return true, 表示已经处理,上层不会再继续执行; false, 上层将继续执行.
	 */
	protected boolean onBackKeyPressed()
	{
		return false;
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

	//	/**
	//	 * 设置卫星按钮的可见性
	//	 *
	//	 * @param visible
	//	 *            true: 显示卫星按钮, false: 隐藏卫星按钮.
	//	 * @Author 12075179
	//	 * @Date 2015-9-24
	//	 */
	//	public void setSatelliteMenuVisible(boolean visible)
	//	{
	//		if (mStatelliteMenu != null)
	//		{
	//			int visibility = visible ? View.VISIBLE : View.GONE;
	//			mStatelliteMenu.setVisibility(visibility);
	//		}
	//	}
	//
	//	/**
	//	 * 设置卫星按钮的[红点]可见性
	//	 *
	//	 * @param visible
	//	 *            true: 显示卫星按钮[红点], false: 隐藏卫星按钮[红点].
	//	 * @Author 12075179
	//	 * @Date 2016-1-18
	//	 */
	//	protected void setSatelliteMenuMarkVisible(boolean visible)
	//	{
	//		if (mStatelliteMenu != null)
	//		{
	//			View redMark = mStatelliteMenu.findViewById(R.id.iv_action_mark);
	//			int visibility = visible ? View.VISIBLE : View.GONE;
	//			redMark.setVisibility(visibility);
	//		}
	//	}
	//
	//	/**
	//	 * @return 卫星按钮[红点]是否显示了
	//	 * @Author 12075179
	//	 * @Date 2016-1-18
	//	 */
	//	private boolean isSatelliteMenuMarkVisible()
	//	{
	//		if (mStatelliteMenu == null)
	//			return false;
	//		View redMark = mStatelliteMenu.findViewById(R.id.iv_action_mark);
	//		return redMark.getVisibility() == View.VISIBLE;
	//	}

	/**
	 * 显示加载框(加载框可以取消)
	 *
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-9-6
	 */
	public void showLoadingView()
	{
		showLoadingView(true);
	}

	/**
	 * 显示加载框
	 *
	 * @param cancelable If true, the dialog is cancelable. The default is true.
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-9-6
	 */
	public void showLoadingView(boolean cancelable)
	{
		// 1,如果销毁了,不用管
		if(isFinishing())
		{
			return;
		}

		if(mLoadingController == null)
		{
			mLoadingController = new LoadingDialog.Controller();
			mLoadingController
					.setOnBackPressedListener(new LoadingDialog.OnBackPressedListener()
					{
						@Override
						public void onBackPressed()
						{
							if(!onBackKeyPressed())
							{
								finish();
							}
						}
					});
		}
		// 2,设置是否可以取消
		mLoadingController.getLoadingDialg().setCancelable(cancelable);
		// 3.1 如果mDialogList为null,说明在onSaveInstanceState之前
		if(mDialogList == null || !isFinishing())
		{
			mLoadingController.show(getFragmentManager());
		}
		// 3.2 如果处于onSaveInstanceState之后, 则不用管
	}

	/**
	 * 隐藏加载框
	 *
	 * @Description:
	 * @Author 12075179
	 * @Date 2015-9-6
	 */
	public void hideLoadingView()
	{
		if(mLoadingController != null)
		{
			mLoadingController.dismiss();
		}
	}

	/**
	 * 弹出信息提示
	 *
	 * @param msgId 信息资源ID
	 * @Author 12075179
	 * @Date 2015-9-22
	 */
	public void displayToast(int msgId)
	{
		displayToast(getText(msgId));
	}

	/**
	 * 弹出信息提示
	 *
	 * @param msg 信息字符串
	 * @Author 12075179
	 * @Date 2015-9-22
	 */
	public void displayToast(CharSequence msg)
	{
		ToastUtils.showMessage(msg);
	}

	/**
	 * 弹出信息对话框
	 *
	 * @param msgId 信息资源ID
	 * @Author 12075179
	 * @Date 2015-9-22
	 */
	public void displayAlertMessag(int msgId)
	{
		displayAlertMessag(getText(msgId));
	}

	/**
	 * 弹出信息对话框
	 *
	 * @param msg 信息字符串
	 * @Author 12075179
	 * @Date 2015-9-22
	 */
	public void displayAlertMessag(CharSequence msg)
	{
		BasicDialogFragment dialog = new CustomDialog.Builder()
				.setMessage(msg)
				.setRightButton(getString(R.string.app_dialog_confirm),null)
				.setCancelable(false).create();
		showDialog(dialog);
	}

	/**
	 * 弹出信息对话框
	 *
	 * @param msg     信息字符串
	 * @param btnText 按钮文字
	 * @Author 12075179
	 * @Date 2015-9-22
	 */
	public void displayAlertMessag(CharSequence msg,CharSequence btnText)
	{
		BasicDialogFragment dialog = new CustomDialog.Builder()
				.setMessage(msg).setRightButton(btnText,null)
				.setCancelable(false).create();
		showDialog(dialog);
	}

	/**
	 * 弹出信息对话框
	 *
	 * @param msg              信息字符串
	 * @param btnText          按钮文字
	 * @param btnClickListener 按钮点击事件
	 * @Author 12075179
	 * @Date 2015-9-22
	 */
	public void displayAlertMessag(CharSequence msg,CharSequence btnText,
			View.OnClickListener btnClickListener)
	{
		BasicDialogFragment dialog = new CustomDialog.Builder()
				.setMessage(msg).setRightButton(btnText,btnClickListener)
				.setCancelable(false).create();
		showDialog(dialog);
	}

	/**
	 * 展示对话框
	 *
	 * @param title              标题,隐藏传null or ""
	 * @param msg                信息,隐藏传null or ""
	 * @param leftBtnText        左边按钮的文字,隐藏传 null or "",默认显示"取消"
	 * @param leftClickListener  左边按钮的点击事件,可为null.
	 * @param rightBtnText       右边按钮的文字,隐藏传 null or "",默认显示"确定"
	 * @param rightClickListener 右边按钮的点击事件,可为null.
	 * @Author 12075179
	 * @Date 2015-9-23
	 */
	public void displayDialog(CharSequence title,CharSequence msg,
			CharSequence leftBtnText,View.OnClickListener leftClickListener,
			CharSequence rightBtnText,View.OnClickListener rightClickListener)
	{
		BasicDialogFragment dialog = new CustomDialog.Builder()
				.setTitle(title).setMessage(msg)
				.setLeftButton(leftBtnText,leftClickListener)
				.setRightButton(rightBtnText,rightClickListener)
				.setCancelable(false).create();
		showDialog(dialog);
	}

	/**
	 * 显示对话框, 该方法可以避免在onSaveInstanceState之后显示的异常.
	 *
	 * @param dialog 被显示的对话框
	 * @Author 12075179
	 * @Date 2015-12-9
	 */
	public void showDialog(BasicDialogFragment dialog)
	{
		// java.lang.IllegalStateException: Activity has been destroyed
		if(isFinishing())
		{
			return;
		}

		if(mDialogList != null)
		{
			mDialogList.add(dialog);
		}
		else
		{
			dialog.show(getFragmentManager(),dialog.getName());
		}
	}

	/**
	 * @param service 服务名称
	 * @return BasicService
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
	 * @Author 12075179
	 * @Date 2015-9-22
	 */
	public int getScreenWidth()
	{
		return getDeviceInfoService().screenWidth;
	}

	/**
	 * @return 屏幕高度
	 * @Author 12075179
	 * @Date 2015-9-22
	 */
	public int getScreenHeight()
	{
		return getDeviceInfoService().screenHeight;
	}

	/**
	 * @return 是否有网络
	 * @Author 12075179
	 * @Date 2015-10-27
	 */
	public boolean isNetworkAvailable()
	{
		NetConnectService netService = (NetConnectService)getService(BasicService.NET_CONNECT);
		return netService.isNetworkAvailable();
	}

	protected void onResume()
	{
		super.onResume();
		// 如果在onSaveInstanceState之后有对话框展示,在resume的时候,显示出来
		if(mDialogList != null)
		{
			for(BasicDialogFragment dialog : mDialogList)
			{
				dialog.show(getFragmentManager(),dialog.getName());
			}
			mDialogList.clear();
			mDialogList = null;
		}
		//		checkIsAutoLoginFail();
		//		// 更新未读消息数量
		//		updatePopupMenu();
		//		// 检查是否有防屏蔽对话框
		//		SecretCodeUtil.checkHasSecretCode(this);
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		mDialogList = new ArrayList<BasicDialogFragment>();
	}

}
