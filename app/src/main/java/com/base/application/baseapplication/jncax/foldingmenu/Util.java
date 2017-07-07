package com.base.application.baseapplication.jncax.foldingmenu;

import android.os.Build;

/**
 * Created by adminchen on 16/1/22 10:38.
 */
public class Util
{
	static final boolean IS_JBMR2 = Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2;
	static final boolean IS_ISC = Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	static final boolean IS_GINGERBREAD_MR1 = Build.VERSION.SDK_INT == Build.VERSION_CODES.GINGERBREAD_MR1;
}
