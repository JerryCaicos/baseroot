package com.base.application.baseapplication.jncax.refresh;

import android.content.Context;
import android.widget.FrameLayout;

/**
 * Created by chenaxing on 2016/12/27.
 */

public class RefreshLoadingRecyclerView extends FrameLayout
{
    public RefreshLoadingRecyclerView(Context context)
    {
        super(context);
    }
}

interface OnRefreshListener
{
    void onRefreshStart();

    void onRefreshing();

    void onRefreshEnd();
}
