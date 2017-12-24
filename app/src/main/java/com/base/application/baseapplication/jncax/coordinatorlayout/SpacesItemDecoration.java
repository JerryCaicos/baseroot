package com.base.application.baseapplication.jncax.coordinatorlayout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by chenaxing on 2017/10/26.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration
{

    private int space;

    public SpacesItemDecoration(int space)
    {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        int spanCount = ((StaggeredGridLayoutManager)parent.getLayoutManager()).getSpanCount();
        if(spanCount == 1){
            outRect.left = 0;
            outRect.right = 0;
            outRect.bottom = 0;
            //注释这两行是为了上下间距相同
            //        if(parent.getChildAdapterPosition(view)==0){
            outRect.top = 0;
            //        }
        } else {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            //注释这两行是为了上下间距相同
            //        if(parent.getChildAdapterPosition(view)==0){
            outRect.top = space;
            //        }
        }

    }
}