package com.base.application.baseapplication.jncax.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
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
    private Context mContext;

    private int maxLine = 99;

    private int horizontalSpac;

    private int verticalSpac;

    private int measureWidth;

    private int paddingLeft;

    private int paddingRight;

    private boolean autoMatchLayout;

    private int currentLine = 0;

    private FlowLayoutAdapter adapter;

    private OnItemClickedListener clickedListener;

    private OnItemLongClickedListener longClickedListener;

    private SparseArray<List<View>> sparseArray;

    //    private List<View> itemList;

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

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);

        maxLine = typedArray.getInt(R.styleable.FlowLayout_maxLine, Integer.MAX_VALUE);
        horizontalSpac = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpac, defaultHorizontal);
        verticalSpac = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpac, defaultVertical);
        autoMatchLayout = typedArray.getBoolean(R.styleable.FlowLayout_autoMatchLayout, false);
        paddingLeft = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_paddingLeft, 0);
        paddingRight = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_paddingRight, 0);
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
        //        if(itemList == null)
        //        {
        //            itemList = new ArrayList<>();
        //        }
        initSparseArray();
        measureLayout();
        addItems();
    }

    private void initSparseArray()
    {
        currentLine = 0;
        sparseArray = new SparseArray<List<View>>();
        sparseArray.put(currentLine, new ArrayList<View>());
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
                sparseArray.put(currentLine, new ArrayList<View>());
            }
            else
            {
                params.leftMargin = lineWidth;
                params.topMargin = lineHeight;

                lineWidth += itemWidth + horizontalSpac;
            }

            itemView.setLayoutParams(params);

            //            itemList.add(itemView);
            sparseArray.get(currentLine).add(itemView);

        }
    }

    private void addItems()
    {
        //        if(itemList == null || itemList.size() <= 0)
        //        {
        //            return;
        //        }
        //        for(View view : itemList)
        //        {
        //            addView(view);
        //        }

        for(int i = 0; i < currentLine + 1; i++)
        {
            List<View> list = sparseArray.get(i);
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
            if(lineSize != 1)
            {
                autoPadding = autoPadding / (lineSize * 2);
            }
            else
            {
                autoPadding = autoPadding / 2;
            }
            int offset = 0;
            for(int j = 0; j < lineSize; j++)
            {
                view = list.get(j);

                RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();

                view.setPadding(view.getPaddingLeft() + autoPadding,
                        view.getPaddingTop(),
                        view.getPaddingRight() + autoPadding,
                        view.getPaddingBottom());
                params.leftMargin += offset;
                offset = (j + 1)* 2 * autoPadding;
//                if(j == 0)
//                {
//                    view.setPadding(view.getPaddingLeft(),
//                            view.getPaddingTop(),
//                            view.getPaddingRight() + autoPadding,
//                            view.getPaddingBottom());
//                }
//                else if(j == lineSize - 1)
//                {
//                    view.setPadding(view.getPaddingLeft() + autoPadding,
//                            view.getPaddingTop(),
//                            view.getPaddingRight(),
//                            view.getPaddingBottom());
//                    params.leftMargin += autoPadding * j * 2 ;
//                }
//                else
//                {
//                    view.setPadding(view.getPaddingLeft() + autoPadding,
//                            view.getPaddingTop(),
//                            view.getPaddingRight() + autoPadding,
//                            view.getPaddingBottom());
//                    params.leftMargin += autoPadding * j * 2;
//                }
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
