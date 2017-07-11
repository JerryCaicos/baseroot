package com.base.application.baseapplication.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by JerryCaicos on 16/1/16.
 */
public class DimenUtils
{

    /**
     * @Description: dip转换为px
     */
    public static int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 得到当前分辨率
     *
     * @param mContext
     * @return
     */
    public static float[] getResolution(Context mContext)
    {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        float width = dm.widthPixels;
        float height = dm.heightPixels;
        float density = dm.density;
        float[] str = {height, width, density};
        return str;
    }

    /**
     * @Description: px转换为dip
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @Description: sp转换为px
     */
    public static int sp2px(Context context, float spValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
