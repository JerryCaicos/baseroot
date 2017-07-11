package com.base.application.baseapplication.jncax.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.base.application.baseapplication.R;
import com.base.application.baseapplication.utils.DimenUtils;

/**
 * Created by JerryCaicos on 2017/7/11.
 */

public class FlowLayout extends ViewGroup
{
    public static final int GRAVITY_LEFT = 0;

    public static final int GRAVITY_CENTER = 1;

    public static final int GRAVITY_RIGHT = 2;

    private Context mContext;

    private int maxLine;

    private int horizontalSpac;

    private int verticalSpac;

    private int layoutGravity;


    public FlowLayout(Context context)
    {
        super(context);
        mContext = context;
    }

    public FlowLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        initAttrs(context,attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context,AttributeSet attrs)
    {
        int defaultHorizontal = DimenUtils.dip2px(context,20);
        int defaultVertical = DimenUtils.dip2px(context,10);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);

        maxLine = typedArray.getInt(R.styleable.FlowLayout_maxLine,Integer.MAX_VALUE);
        horizontalSpac = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpac,defaultHorizontal);
        verticalSpac = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpac,defaultVertical);
        layoutGravity = typedArray.getInt(R.styleable.FlowLayout_layoutGravity,0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {

    }
}
