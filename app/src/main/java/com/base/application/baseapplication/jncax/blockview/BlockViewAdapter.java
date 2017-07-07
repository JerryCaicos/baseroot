package com.base.application.baseapplication.jncax.blockview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.base.application.baseapplication.R;

import java.util.List;

/**
 * Created by adminchen on 16/1/20 14:59.
 */
public class BlockViewAdapter extends BaseAdapter
{
	private Context mContext;

	private List<BlockModel> list;

	public BlockViewAdapter(Context context)
	{
		mContext = context;
	}

	public void setLableList(List<BlockModel> lable)
	{
		list = lable;
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int i)
	{
		return list.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return i;
	}

	@Override
	public View getView(final int i,View view,ViewGroup viewGroup)
	{
		ViewHolder holder = null;
		if(view == null)
		{
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.view_block_view_item,null);
			holder.mItemView = (TextView)view.findViewById(R.id.view_block_view_item);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)view.getTag();
		}
		holder.mItemView.setText(list.get(i).getBlockname());
		if(list.get(i).isChecked())
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
				list.get(i).setIsChecked();
				if(list.get(i).isChecked())
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
