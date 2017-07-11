package com.base.application.baseapplication.custom.header;

/**
 * Created by JerryCaicos on 16/1/16.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.application.baseapplication.R;

/**
 * 界面通用头部, 样式如下:
 * <PRE>
 * ________________________________________________
 * |                                                |
 * | ＜-           Title          action1   action2  |
 * |________________________________________________|
 * ↑              ↑               ↑        ↑
 * BackAction      Title          action    action
 * </PRE>
 * <B>左边和中间是固定的</B>, 右边的action是可以动态修改的.
 */
public final class HeaderBuilder
{

	/**
	 * 标题View
	 **/
	private ViewGroup mHeaderView;

	private Context mContext;

	@SuppressLint("InflateParams")
	public HeaderBuilder(Context context)
	{
		mContext = context;
		// 获取标题View
		mHeaderView = (ViewGroup)LayoutInflater.from(mContext).inflate(
				R.layout.layout_common_header,null);
	}

	/**
	 * 设置【返回按钮】的点击事件
	 *
	 * @param listener 返回按钮的点击事件
	 */
	public void setBackActionListener(View.OnClickListener listener)
	{
		View backView = mHeaderView.findViewById(R.id.iv_back);
		backView.setOnClickListener(listener);
	}

	/**
	 * 设置返回按钮的可见性
	 *
	 * @param visibility 可见性
	 */
	public void setBackActionVisibility(int visibility)
	{
		View backView = mHeaderView.findViewById(R.id.iv_back);
		backView.setVisibility(visibility);
	}

	/**
	 * 设置标题
	 *
	 * @param titleResId 标题资源ID
	 */
	public void setTitle(int titleResId)
	{
		setTitle(mContext.getText(titleResId));
	}

	/**
	 * 设置标题
	 *
	 * @param title 标题字符串
	 */
	public void setTitle(CharSequence title)
	{
		TextView titleView = (TextView)mHeaderView.findViewById(R.id.tv_title);
		titleView.setText(title);
	}

	/**
	 * 设置标题的可见性
	 *
	 * @param visibility 可见性
	 */
	public void setTitleVisibility(int visibility)
	{
		TextView titleView = (TextView)mHeaderView.findViewById(R.id.tv_title);
		titleView.setVisibility(visibility);
	}

	/**
	 * <B>右边</B>添加文字操作,eg: 编辑
	 *
	 * @param nameResId 名称资源ID
	 * @param listener  点击的事件
	 * @return 添加之后的TextView
	 */
	public TextView addTextAction(int nameResId,View.OnClickListener listener)
	{
		return addTextAction(mContext.getText(nameResId),listener);
	}

	/**
	 * <B>右边</B>添加文字操作,eg: 编辑
	 *
	 * @param name     名称字符串
	 * @param listener 点击的事件
	 * @return 添加之后的TextView
	 */
	public TextView addTextAction(CharSequence name,View.OnClickListener listener)
	{
		TextView actionView = (TextView)addActionView(R.layout.view_header_action_text);
		actionView.setText(name);
		actionView.setOnClickListener(listener);
		return actionView;
	}

	/**
	 * <B>右边</B>添加icon操作,eg: 。。。(菜单)
	 *
	 * @param iconResId icon资源ID
	 * @param listener  点击的事件
	 * @return 添加之后的ImageView
	 */
	public ImageView addIconAction(int iconResId,View.OnClickListener listener)
	{
		return addIconAction(mContext.getResources().getDrawable(iconResId),listener);
	}

	/**
	 * <B>右边</B>添加icon操作,eg: 。。。(菜单)
	 *
	 * @param icon     icon
	 * @param listener 点击的事件
	 * @return 添加之后的ImageView
	 */
	public ImageView addIconAction(Drawable icon,View.OnClickListener listener)
	{
		ImageView actionView = (ImageView)addActionView(R.layout.view_header_action_icon);
		actionView.setImageDrawable(icon);
		actionView.setOnClickListener(listener);
		return actionView;
	}

	/**
	 * <B>右边</B>添加actionView
	 *
	 * @param view 一个actionView
	 */
	public void addActionView(View view)
	{
		// 获取Action容器
		ViewGroup actionsLayout = (ViewGroup)mHeaderView.findViewById(R.id.ll_actions);
		// 添加ActionView
		actionsLayout.addView(view,new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
	}

	/**
	 * @param actionResId 被添加action的布局资源ID
	 * @return 添加之后的View
	 */
	private View addActionView(int actionResId)
	{
		// 获取Action容器
		ViewGroup actionsLayout = (ViewGroup)mHeaderView.findViewById(R.id.ll_actions);
		// 获取ActionView
		View actionView = LayoutInflater.from(mContext).inflate(actionResId,null);
		// 添加ActionView
		actionsLayout.addView(actionView,new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		// 返回ActionView
		return actionView;
	}

	/**
	 * @return 标题View
	 */
	public View getHeaderView()
	{
		return mHeaderView;
	}
}

