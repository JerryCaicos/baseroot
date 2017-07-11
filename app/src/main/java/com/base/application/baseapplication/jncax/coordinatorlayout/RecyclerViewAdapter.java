package com.base.application.baseapplication.jncax.coordinatorlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.application.baseapplication.R;

import java.util.List;

/**
 * Created by JerryCaicos on 16/9/27 11:29.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>
{
	private Context mContext;

	private List<String> list;

	public RecyclerViewAdapter(Context context,List<String> l)
	{
		mContext = context;
		list = l;
	}

	@Override
	public int getItemViewType(int position)
	{
		return super.getItemViewType(position);
	}

	@Override
	public long getItemId(int position)
	{
		return super.getItemId(position);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.acjn_view_recycler_item,null);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder,int position)
	{
		holder.textView.setText(list.get(position));
	}

	@Override
	public int getItemCount()
	{
		return list.size();
	}
}

class ViewHolder extends RecyclerView.ViewHolder
{
	public TextView textView;

	public ViewHolder(View itemView)
	{
		super(itemView);
		textView = (TextView)itemView.findViewById(R.id.item_view);
	}
}
