package com.base.application.baseapplication.jncax.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;

import com.base.application.baseapplication.R;
import com.base.application.baseapplication.utils.DimenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryCaicos on 2017/7/11.
 */

public class FlowLayout extends RelativeLayout
{
    public static final int GRAVITY_CENTER = 0;

    public static final int GRAVITY_SIDES = 1;

    public static final int DEFAULT_LINE = 99;

    public static final int DEFAULT_MAX_LINE = 99;

    private Context mContext;

    private int maxLine;

    private int defaultLine;

    private int horizontalSpac;

    private int verticalSpac;

    private int measureWidth;

    private int paddingLeft;

    private int paddingRight;

    private boolean autoMatchLayout;

    private int currentLine = 0;

    private int layoutGravity;

    private boolean showWithDefault = false;

    private Drawable flowMoreIcon;

    private FlowLayoutAdapter adapter;

    private OnItemClickedListener clickedListener;

    private OnItemLongClickedListener longClickedListener;

    private SparseArray<List<View>> defaultList;

    private SparseArray<List<View>> maxList;

    public FlowLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs)
    {
        int defaultHorizontal = DimenUtils.dip2px(context, 20);
        int defaultVertical = DimenUtils.dip2px(context, 10);

        try
        {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);

            maxLine = typedArray.getInt(R.styleable.FlowLayout_maxLine, DEFAULT_MAX_LINE);
            defaultLine = typedArray.getInt(R.styleable.FlowLayout_defaultLine, DEFAULT_LINE);
            horizontalSpac = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpac, defaultHorizontal);
            verticalSpac = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpac, defaultVertical);
            autoMatchLayout = typedArray.getBoolean(R.styleable.FlowLayout_autoMatchLayout, false);
            paddingLeft = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_paddingLeft, 0);
            paddingRight = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_paddingRight, 0);
            layoutGravity = typedArray.getInt(R.styleable.FlowLayout_layoutGravity, 0);
            flowMoreIcon = typedArray.getDrawable(R.styleable.FlowLayout_flowMoreIcon);

            typedArray.recycle();
        }
        catch(Exception e)
        {
        }
    }

    public void setDefaultLine(int defaultLine)
    {
        this.defaultLine = defaultLine;
    }

    public void setMaxLine(int maxLine)
    {
        this.maxLine = maxLine;
    }

    public void setHorizontalSpac(int horizontalSpac)
    {
        this.horizontalSpac = horizontalSpac;
    }

    public void setVerticalSpac(int verticalSpac)
    {
        this.verticalSpac = verticalSpac;
    }

    public void setClickedListener(OnItemClickedListener clickedListener)
    {
        this.clickedListener = clickedListener;
    }

    public void setLongClickedListener(OnItemLongClickedListener longClickedListener)
    {
        this.longClickedListener = longClickedListener;
    }

    public void setPaddingLeft(int paddingLeft)
    {
        this.paddingLeft = paddingLeft;
    }

    public void setPaddingRight(int paddingRight)
    {
        this.paddingRight = paddingRight;
    }

    public void setAutoMatchLayout(boolean autoMatchLayout)
    {
        this.autoMatchLayout = autoMatchLayout;
    }

    public void setLayoutGravity(int layoutGravity)
    {
        this.layoutGravity = layoutGravity;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    public void setAdapter(FlowLayoutAdapter adapter)
    {
        removeAllViews();
        this.adapter = adapter;
        initSparseArray();
        measureLayout();
        addItems();
    }

    private void initSparseArray()
    {
        currentLine = 0;
        maxList = new SparseArray<List<View>>();
        maxList.put(currentLine, new ArrayList<View>());
    }

    private void measureLayout()
    {
        /**每行标签的最宽宽度**/
        int lineWidth = paddingLeft;
        /**每行标签的最高高度**/
        int lineHeight = 0;

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

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);

            int itemWidth = itemView.getMeasuredWidth();
            int itemHeight = itemView.getMeasuredHeight();

            //当前绘制的x坐标 + 标签宽度如果大于父布局宽度，则说明需要换行绘制
            if(lineWidth + itemWidth > measureWidth - paddingLeft - paddingRight)
            {
                lineHeight += itemHeight + verticalSpac;
                lineWidth = paddingLeft;
                params.leftMargin = lineWidth;
                params.topMargin = lineHeight;

                lineWidth += itemWidth + horizontalSpac;

                currentLine++;
                maxList.put(currentLine, new ArrayList<View>());
            }
            else
            {
                params.leftMargin = lineWidth;
                params.topMargin = lineHeight;

                lineWidth += itemWidth + horizontalSpac;
            }

            itemView.setLayoutParams(params);
            maxList.get(currentLine).add(itemView);

            if(currentLine >= maxLine)
            {
                maxList.remove(currentLine);
                currentLine -= 1;
                break;
            }
        }

        getDefaultList();
    }

    private void getDefaultList()
    {
        int length = maxList.size();
        if(defaultLine < length
                && defaultLine != DEFAULT_LINE
                && defaultLine != maxLine
                && defaultLine < maxLine)
        {
            defaultList = new SparseArray<>();
            defaultList = maxList.clone();
            showWithDefault = true;
            for(int i = defaultLine; i < length; i++)
            {
                defaultList.remove(i);
            }
        }
    }

    private void addItems()
    {
        SparseArray<List<View>> tempList = new SparseArray<>();
        if(showWithDefault)
        {
            tempList = defaultList;
        }
        else
        {
            tempList = maxList;
        }
        int length = tempList.size();
        for(int i = 0; i < length + 1; i++)
        {
            List<View> list = tempList.get(i);
            if(list == null)
            {
                continue;
            }
            addLineItem(list);
        }
    }

    private void addLineItem(List<View> list)
    {
        View view;
        int lineSize = list.size();
        if(autoMatchLayout)
        {
            int autoPadding = 0;
            View lastView = list.get(lineSize - 1);
            RelativeLayout.LayoutParams lastParam = (LayoutParams) lastView.getLayoutParams();
            autoPadding = measureWidth - lastView.getMeasuredWidth() - lastParam.leftMargin - paddingLeft;
            if(layoutGravity == GRAVITY_CENTER)
            {
                if(lineSize != 1)
                {
                    autoPadding = autoPadding / (lineSize * 2);
                }
                else
                {
                    autoPadding = autoPadding / 2;
                }
            }
            else
            {
                if(lineSize != 1)
                {
                    autoPadding = autoPadding / (lineSize * 2 - 2);
                }
                else
                {
                    autoPadding = autoPadding / 2;
                }
            }
            int offset = 0;
            for(int j = 0; j < lineSize; j++)
            {
                view = list.get(j);

                RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
                if(layoutGravity == GRAVITY_CENTER)
                {
                    view.setPadding(view.getPaddingLeft() + autoPadding,
                            view.getPaddingTop(),
                            view.getPaddingRight() + autoPadding,
                            view.getPaddingBottom());
                    params.leftMargin += offset;
                    offset = (j + 1) * 2 * autoPadding;
                }
                else
                {
                    if(j == 0)
                    {
                        view.setPadding(view.getPaddingLeft(),
                                view.getPaddingTop(),
                                view.getPaddingRight() + autoPadding,
                                view.getPaddingBottom());
                        offset = autoPadding;
                    }
                    else if(j == lineSize - 1)
                    {
                        view.setPadding(view.getPaddingLeft() + autoPadding,
                                view.getPaddingTop(),
                                view.getPaddingRight(),
                                view.getPaddingBottom());
                        params.leftMargin += offset;
                    }
                    else
                    {
                        view.setPadding(view.getPaddingLeft() + autoPadding,
                                view.getPaddingTop(),
                                view.getPaddingRight() + autoPadding,
                                view.getPaddingBottom());
                        params.leftMargin += offset;
                        offset = (j * 2 + 1) * autoPadding;
                    }
                }
                addView(view);
            }
        }
        else
        {
            for(int j = 0; j < lineSize; j++)
            {
                view = list.get(j);
                addView(view);
            }
        }

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
