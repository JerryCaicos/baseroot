package com.base.application.baseapplication.tabhost;

import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by adminchen on 16/1/16.
 */
public class BasicTab
{
	private TabHost.TabSpec tabSpec;
	private Class<?> tabClass;
	private Bundle argments;

	public BasicTab(TabHost.TabSpec tabSpec,Class<?> tabClass,Bundle argments)
	{
		this.tabSpec = tabSpec;
		this.tabClass = tabClass;
		this.argments = argments;
	}

	public TabHost.TabSpec getTabSpec()
	{
		return tabSpec;
	}

	public Class<?> getTabClass()
	{
		return tabClass;
	}

	public Bundle getArgments()
	{
		return argments;
	}
}
