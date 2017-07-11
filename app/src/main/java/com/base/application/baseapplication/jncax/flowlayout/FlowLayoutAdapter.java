package com.base.application.baseapplication.jncax.flowlayout;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by JerryCaicos on 2017/7/11.
 */

public abstract class FlowLayoutAdapter extends BaseAdapter
{
    protected List<FlowModel> flowModelList;

    protected Context mContext;

    public FlowLayoutAdapter(Context context)
    {
        mContext = context;
    }

    public void setFlowModelList(List<FlowModel> list)
    {
        flowModelList = list;
    }
}
