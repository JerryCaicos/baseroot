package com.base.application.baseapplication.jncax.flowlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.application.baseapplication.R;

/**
 * Created by chenaxing on 2017/7/12.
 */

public class FlowAdapter extends FlowLayoutAdapter
{
    public FlowAdapter(Context context)
    {
        super(context);
    }

    @Override
    public int getCount()
    {
        return flowModelList.size();
    }

    @Override
    public Object getItem(int i)
    {
        return flowModelList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        ViewHolder holder = null;
        if(view == null)
        {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.view_block_view_item, null);
            holder.mItemView = (TextView) view.findViewById(R.id.view_block_view_item);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        holder.mItemView.setText(flowModelList.get(i).getItemName());
        if(flowModelList.get(i).isChecked())
        {
            holder.mItemView.setBackgroundResource(R.drawable.acjn_eva_tag_select);
        }
        else
        {
            holder.mItemView.setBackgroundResource(R.drawable.acjn_grey_normal);
        }
        holder.mItemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                flowModelList.get(i).setChecked();
                if(flowModelList.get(i).isChecked())
                {
                    view.setBackgroundResource(R.drawable.acjn_eva_tag_select);
                }
                else
                {
                    view.setBackgroundResource(R.drawable.acjn_grey_normal);
                }
            }
        });
        return view;
    }

    class ViewHolder
    {
        TextView mItemView;
    }
}
