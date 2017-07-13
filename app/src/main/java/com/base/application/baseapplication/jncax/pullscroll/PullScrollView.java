package com.base.application.baseapplication.jncax.pullscroll;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by JerryCaicos on 2017/7/13.
 */

public class PullScrollView extends ScrollView
{
    private View mRootView;

    private float positionY;

    private Rect origonRect = new Rect();

    @Override
    protected void onFinishInflate()
    {
        if(getChildCount() > 0)
        {
            mRootView = getChildAt(0);
        }
        super.onFinishInflate();
    }

    public PullScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if(mRootView != null)
        {
            disposeOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);

    }

    private void disposeOnTouchEvent(MotionEvent event)
    {

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                positionY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if(isNeedAnimation())
                {
                    translateLayout();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int offset = (int) ((event.getY() - positionY) / 3);
                positionY = event.getY();
                if(isNeedMove())
                {
                    if(origonRect.isEmpty())
                    {
                        origonRect.set(mRootView.getLeft(),
                                mRootView.getTop(),
                                mRootView.getRight(),
                                mRootView.getBottom());
                        return;
                    }
                    mRootView.layout(mRootView.getLeft(),
                            mRootView.getTop() + offset,
                            mRootView.getRight(),
                            mRootView.getBottom() + offset);
                }
                break;
        }
    }

    private void translateLayout()
    {
        TranslateAnimation animation = new TranslateAnimation(0, 0, mRootView.getTop() - origonRect.top, 0);
        animation.setDuration(200);
        mRootView.startAnimation(animation);
        mRootView.layout(origonRect.left, origonRect.top, origonRect.right, origonRect.bottom);
        origonRect.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation()
    {
        return !origonRect.isEmpty();
    }


    // 是否需要移动布局
    public boolean isNeedMove()
    {
        int offset = mRootView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if(scrollY == 0 || offset == scrollY)
        {
            return true;
        }
        return false;
    }
}
