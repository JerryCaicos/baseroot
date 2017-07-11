package com.base.application.baseapplication.jncax.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
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

    private FlowLayoutAdapter adapter;

    private OnItemClickedListener clickedListener;

    private OnItemLongClickedListener longClickedListener;


    public FlowLayout(Context context)
    {
        super(context);
        mContext = context;
    }

    public FlowLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        initAttrs(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs)
    {
        int defaultHorizontal = DimenUtils.dip2px(context, 20);
        int defaultVertical = DimenUtils.dip2px(context, 10);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);

        maxLine = typedArray.getInt(R.styleable.FlowLayout_maxLine, Integer.MAX_VALUE);
        horizontalSpac = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpac, defaultHorizontal);
        verticalSpac = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpac, defaultVertical);
        layoutGravity = typedArray.getInt(R.styleable.FlowLayout_layoutGravity, 0);
    }

    public int getMaxLine()
    {
        return maxLine;
    }

    public void setMaxLine(int maxLine)
    {
        this.maxLine = maxLine;
    }

    public int getHorizontalSpac()
    {
        return horizontalSpac;
    }

    public void setHorizontalSpac(int horizontalSpac)
    {
        this.horizontalSpac = horizontalSpac;
    }

    public int getVerticalSpac()
    {
        return verticalSpac;
    }

    public void setVerticalSpac(int verticalSpac)
    {
        this.verticalSpac = verticalSpac;
    }

    public int getLayoutGravity()
    {
        return layoutGravity;
    }

    public void setLayoutGravity(int layoutGravity)
    {
        this.layoutGravity = layoutGravity;
    }

    public FlowLayoutAdapter getAdapter()
    {
        return adapter;
    }

    public void setAdapter(FlowLayoutAdapter adapter)
    {
        this.adapter = adapter;
        requestLayout();
    }

    public OnItemClickedListener getClickedListener()
    {
        return clickedListener;
    }

    public void setClickedListener(OnItemClickedListener clickedListener)
    {
        this.clickedListener = clickedListener;
    }

    public OnItemLongClickedListener getLongClickedListener()
    {
        return longClickedListener;
    }

    public void setLongClickedListener(OnItemLongClickedListener longClickedListener)
    {
        this.longClickedListener = longClickedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if(adapter == null)
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        else
        {
            measureLayout(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureLayout(int widthMeasureSpec, int heightMeasureSpec)
    {
        /**FlowLayout控件的宽度**/
        int width = horizontalSpac;
        /**FlowLayout控件的高度**/
        int height = verticalSpac;
        /**每行标签的最宽宽度**/
        int lineWidth = 0;
        /**每行标签的最高高度**/
        int lineHeight = 0;

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measuerHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int count = adapter.getCount();
        View itemView;
        for(int i = 0; i < count; i++)
        {
            itemView = adapter.getView(i, null, null);
            itemView.setTag(i);
            itemView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(clickedListener != null)
                    {
                        clickedListener.onItemClicked(view, (Integer) view.getTag());
                    }
                }
            });

            itemView.setOnLongClickListener(new OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    if(longClickedListener != null)
                    {
                        longClickedListener.onItemLongClicked(view, (Integer) view.getTag());
                    }
                    return true;
                }
            });

            itemView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            MarginLayoutParams params = (MarginLayoutParams) itemView.getLayoutParams();

            /**当前标签的宽度**/
            int itemWidth = itemView.getMeasuredWidth();
            /**当前标签的高度**/
            int itemHeight = itemView.getMeasuredHeight();

            //当前绘制的x坐标 + 标签宽度如果大于父布局宽度，则说明需要换行绘制
            if(lineWidth + itemWidth > measureWidth)
            {
                params.leftMargin = horizontalSpac;
                params.topMargin = lineHeight + verticalSpac;

                width = Math.max(lineWidth, width);
                height += lineHeight + verticalSpac;

                lineHeight += itemHeight + verticalSpac;
                lineWidth = itemWidth + horizontalSpac;
            }
            else
            {
                params.leftMargin = lineWidth;
                params.topMargin = height;

                lineWidth += itemWidth + horizontalSpac;
                lineHeight = Math.max(lineHeight, itemHeight + verticalSpac);
            }

            itemView.setLayoutParams(params);

            if(i == count - 1)
            {
                height += verticalSpac;
                width = Math.max(width, lineWidth);
            }
        }

        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth : width,
                (measuerHeightMode == MeasureSpec.EXACTLY) ? measureHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        if(adapter != null)
        {
            int count = adapter.getCount();
            View itemView;
            for(int i = 0; i < count; i++)
            {
                itemView = adapter.getView(i, null, null);
                MarginLayoutParams params = (MarginLayoutParams) itemView.getLayoutParams();
                itemView.layout(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
            }
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p)
    {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public interface OnItemClickedListener
    {
        void onItemClicked(View view, int position);
    }

    public interface OnItemLongClickedListener
    {
        void onItemLongClicked(View view, int position);
    }
}
