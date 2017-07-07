package com.base.application.baseapplication.jncax.expandtable.adapter;


import android.content.Context;
import android.view.View;

import com.base.application.baseapplication.R;
import com.base.application.baseapplication.jncax.expandtable.model.BaseData;

import java.util.List;

/**
 * Created by AAA on 2015/6/16.
 */
public class ExpandGridAdapter extends CommonAdapter<BaseData>
{
	private OnClick listener;

	public ExpandGridAdapter(Context context,List<BaseData> mDatas,
			int itemLayoutId)
	{
		super(context,mDatas,itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper,final BaseData item)
	{
		helper.setText(R.id.expand_grid_txt,item.getName());
		View view = helper.getView(R.id.expand_grid_txt);
		view.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(listener != null)
				{
					listener.onClick(item.getDataId(),item.getName());
				}

			}
		});
	}

	public interface OnClick
	{
		void onClick(int dataId,String str);
	}

	public void setOnClick(OnClick listener)
	{
		this.listener = listener;
	}
}
