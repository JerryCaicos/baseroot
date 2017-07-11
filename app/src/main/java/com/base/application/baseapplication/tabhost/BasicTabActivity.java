package com.base.application.baseapplication.tabhost;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.base.application.baseapplication.BasicActivity;
import com.base.application.baseapplication.R;

import java.util.List;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public abstract class BasicTabActivity extends BasicActivity
{

	/**
	 * {@link BasicTabHost}
	 **/
	private BasicTabHost mTabHost;
	/**
	 * 大图标
	 **/
	private TabWidget mTWLarger;
	/**
	 * Tab准备要切换的Index
	 **/
	private int mPreIndex = -1;
	/**
	 * 是否处于{@link #onSaveInstanceState(Bundle)}之后
	 **/
	private boolean isAfterOnSaveInstanceState;
	/**
	 * 是否Pause过,如果走过onPause,回来的时候需要调用BasicTabFragment的onShow.
	 **/
	private boolean hasPaused;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		// 获取TabHost
		mTabHost = (BasicTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this,getFragmentManager(),R.id.realtabcontent);
		// 获取大图标
		mTWLarger = (TabWidget)findViewById(R.id.tw_main_larger);
		mTWLarger.setStripEnabled(false);
		// 添加TabItem
		List<BasicTab> tabList = getTabList();
		if(tabList != null && !tabList.isEmpty())
		{
			int size = tabList.size();
			BasicTab tab;
			for(int i = 0;i < size;i++)
			{
				tab = tabList.get(i);
				mTabHost.addTab(tab.getTabSpec(),tab.getTabClass(),tab.getArgments());
				// 多少个小图标,就添加多少个大图标
				View child = getLargerTabView();
				child.setTag(Integer.valueOf(i));
				mTWLarger.addView(child);
				// 覆盖TabWidget的设置
				child.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Integer index = (Integer)v.getTag();
						setCurrentTab(index.intValue());
					}
				});
			}
		}
	}

	/**
	 * @return 大图标
	 */
	private ImageView getLargerTabView()
	{
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		disabledTab(imageView);
		return imageView;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(isAfterOnSaveInstanceState)
		{
			// in order to throw the exception "can't perform this action after onSaveInstance."
			isAfterOnSaveInstanceState = false;
			if(mPreIndex >= 0)
			{
				if(mTabHost.getCurrentTab() != mPreIndex)
				{
					// 如果tab不一样,不要走下面的调用show方法,tab切换的时候会自动调用show方法
					setCurrentTab(mPreIndex);
					// 重置变量
					mPreIndex = -1;
					hasPaused = false;
					return;
				}
				mPreIndex = -1;
			}
		}
		if(hasPaused)
		{
			hasPaused = false;
			// 如果pause过了,需要调用当前TabFragment的onShow方法
			Fragment f = mTabHost.getCurrentTabFragment();
			if(f != null && f instanceof BasicTabFragment)
			{
				((BasicTabFragment)f).onShow();
			}
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		hasPaused = true;
		// 调用当前Fragment的onHide()
		Fragment f = mTabHost.getCurrentTabFragment();
		if(f != null && f instanceof BasicTabFragment)
		{
			((BasicTabFragment)f).onHide();
		}
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		// in order to throw the exception "can't perform this action after onSaveInstance."
		isAfterOnSaveInstanceState = false;
		if(mPreIndex >= 0)
		{
			setCurrentTab(mPreIndex);
			mPreIndex = -1;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		isAfterOnSaveInstanceState = true;
	}

	/**
	 * 设置Tab的高度
	 *
	 * @param height tab的高度
	 */
	protected final void setTabHeight(int height)
	{
		if(mTabHost == null)
		{
			return;
		}
		mTabHost.getTabWidget().getLayoutParams().height = height;
		// 比例,适配所有屏幕
		mTWLarger.getLayoutParams().height = (int)(height * 1.2);
	}

	/**
	 * @return Tab的高度
	 */
	public final int getTabHeight()
	{
		if(mTabHost == null)
		{
			return 0;
		}
		return mTabHost.getTabWidget().getLayoutParams().height;
	}

	/**
	 * 设置当前Tab
	 *
	 * @param index tab对应的index
	 * @Description:
	 */
	public final void setCurrentTab(int index)
	{
		if(isAfterOnSaveInstanceState)
		{
			mPreIndex = index;
			return;
		}
		mTabHost.setCurrentTab(index);
	}

	/**
	 * 同步大图标
	 *
	 * @param index
	 */
	private final void synchronizeLargetTab(int index)
	{
		int count = mTWLarger.getChildCount();
		View tab;
		for(int i = 0;i < count;i++)
		{
			tab = mTWLarger.getChildAt(i);
			if(tab == null)
			{
				continue;
			}

			if(i == index && !tab.isSelected())
			{
				tab.setSelected(true);
			}
			else if(i != index && tab.isSelected())
			{
				tab.setSelected(false);
			}
		}
	}

	/**
	 * 设置大图标
	 *
	 * @param index
	 * @param bmPressed 按下图片
	 * @param bmNormal  正常的图片
	 */
	public final void setLargerTab(int index,Bitmap bmPressed,Bitmap bmNormal)
	{
		if(index < 0 || index >= mTWLarger.getChildCount())
		{
			return;
		}
		// 显示大图标
		ImageView largerTab = (ImageView)mTWLarger.getChildTabViewAt(index);
		enabledTab(largerTab);
		Drawable drawablePressed = new BitmapDrawable(getResources(),bmPressed);
		Drawable drawableNormal = new BitmapDrawable(getResources(),bmNormal);
		largerTab.setImageDrawable(getStateListDrawable(drawablePressed,drawableNormal));
		// 隐藏小图标
		View smallTab = mTabHost.getTabWidget().getChildTabViewAt(index);
		disabledTab(smallTab);
	}

	/**
	 * @param drawablePressed
	 * @param drawableNormal
	 * @return {@link StateListDrawable}
	 */
	private static Drawable getStateListDrawable(Drawable drawablePressed,Drawable drawableNormal)
	{
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_selected,android.R.attr.state_enabled},
				drawablePressed);
		drawable.addState(new int[]{android.R.attr.state_pressed,android.R.attr.state_enabled},
				drawablePressed);
		drawable.addState(new int[]{android.R.attr.state_focused,android.R.attr.state_enabled},
				drawablePressed);
		drawable.addState(new int[]{android.R.attr.state_focused},drawablePressed);
		drawable.addState(new int[]{android.R.attr.state_enabled},drawableNormal);
		drawable.addState(new int[]{},drawableNormal);
		return drawable;
	}

	/**
	 * 清除大图标
	 *
	 * @param index
	 */
	public final void clearLargerTab(int index)
	{
		if(index < 0 || index >= mTWLarger.getChildCount())
		{
			return;
		}
		// 隐藏大图标
		View largerTab = mTWLarger.getChildTabViewAt(index);
		disabledTab(largerTab);
		// 显示小图标
		View smallTab = mTabHost.getTabWidget().getChildTabViewAt(index);
		enabledTab(smallTab);
	}

	/**
	 * 激活Tab
	 *
	 * @param tab
	 */
	private void enabledTab(View tab)
	{
		tab.setVisibility(View.VISIBLE);
		tab.setEnabled(true);
	}

	/**
	 * 冻结Tab
	 *
	 * @param tab
	 */
	private void disabledTab(View tab)
	{
		tab.setVisibility(View.INVISIBLE);
		tab.setEnabled(false);
	}

	/**
	 * 获取tab图标的View, 需要判空.
	 *
	 * @param index
	 * @return
	 */
	public ImageButton getTabView(int index)
	{
		TabWidget tabWidget = mTabHost.getTabWidget();
		if(index < 0 || index >= tabWidget.getChildCount())
		{
			return null;
		}
		View tab = tabWidget.getChildTabViewAt(index);
		if(tab != null && tab instanceof ImageButton)
		{
			return (ImageButton)tab;
		}
		return null;
	}

	/**
	 * 更新图标+内容
	 *
	 * @param index       更新的位置
	 * @param indicatorId 图标对应的资源ID.
	 * @param tabClass    Tab对应的Fragment.class,须为BasicTabFragment的子类
	 * @param argments    传给Tab对应Fragment的Arguments
	 */
	public void updateTab(int index,int indicatorId,Class<?> tabClass,Bundle argments)
	{
		if(index < 0 || index >= mTabHost.getTabWidget().getChildCount())
		{
			return;
		}
		ImageButton tabView = getTabView(index);
		if(tabView == null)
		{
			return;
		}

		tabView.setImageResource(indicatorId);
		mTabHost.updateTab(index,tabClass,argments);
	}

	/**
	 * 设置当前Tab
	 *
	 * @param tag tab对应的tag
	 * @Description:
	 */
	protected final void setCurrentTabByTag(String tag)
	{
		mTabHost.setCurrentTabByTag(tag);
	}

	/**
	 * @return 当前的tab
	 * @Description:
	 */
	protected final int getCurrentTab()
	{
		return mTabHost.getCurrentTab();
	}


	/**
	 * 创建一个{@link TabHost.TabSpec}
	 *
	 * @param tag required tag of tab.
	 * @Description:
	 * @see TabHost#newTabSpec(String)
	 */
	protected TabHost.TabSpec createTabSpec(String tag)
	{
		return mTabHost.newTabSpec(tag);
	}

	/**
	 * 构造{@link BasicTab}需要{@link TabHost.TabSpec},
	 * 可以通过{@link #createTabSpec(String)}创建一个{@link TabHost.TabSpec}.
	 *
	 * @return {@link BasicTab}列表
	 * @Description:
	 */
	protected abstract List<BasicTab> getTabList();

	/**
	 * 设置tab切换事件监听
	 *
	 * @param l {@link BasicTabHost.OnPreTabChangeListener}
	 */
	protected void setOnPreTabChangedListener(BasicTabHost.OnPreTabChangeListener l)
	{
		mTabHost.setOnPreTabChangedListener(l);
	}

	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		super.onActivityResult(requestCode,resultCode,data);
		// 让Fragment收到消息
		mTabHost.performActivityResult(requestCode,resultCode,data);
	}
}

