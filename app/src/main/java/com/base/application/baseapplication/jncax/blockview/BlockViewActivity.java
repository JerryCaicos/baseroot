package com.base.application.baseapplication.jncax.blockview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;


import com.base.application.baseapplication.R;
import com.base.application.baseapplication.jncax.flowlayout.FlowAdapter;
import com.base.application.baseapplication.jncax.flowlayout.FlowLayout;
import com.base.application.baseapplication.jncax.flowlayout.FlowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryCaicos on 16/1/20 10:56.
 */
public class BlockViewActivity extends Activity
{
	private BlockView mBlockView;

	private BlockViewAdapter mBlockViewAdapter;

    private FlowLayout mFlowLayout;

    private FlowAdapter mFlowAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acjn_activity_block_view);

		mBlockView = (BlockView)findViewById(R.id.block_view);
		mFlowLayout = (FlowLayout)findViewById(R.id.flow_view);
	}

	private void showlables()
	{
		String str = "标01";
		String str1 = "标签标02";
		String str2 = "标签标签标03";
		String str3 = "标签标签标签标04";
		String str4 = "标签标05";
		String str5 = "标签标06";
		String str6 = "标07";
		String str10 = "08";
		String str7 = "标签标签标签标09";
		String str8 = "标签标签标签标签标签标10";
		String str9 = "标签标签标11";
		List<String> strings = new ArrayList<>();
		strings.add(str);
		strings.add(str1);
		strings.add(str2);
		strings.add(str3);
		strings.add(str4);
		strings.add(str5);
		strings.add(str6);
		strings.add(str10);
		strings.add(str7);
		strings.add(str8);
		strings.add(str9);
		List<BlockModel> lable = new ArrayList<BlockModel>();
		BlockModel blockModel = null;
		for(int i = 0;i < strings.size();i++)
		{
			blockModel = new BlockModel(strings.get(i),false);
			lable.add(blockModel);
		}

		mBlockViewAdapter = new BlockViewAdapter(this);
		mBlockViewAdapter.setLableList(lable);
		mBlockView.setAdapter(mBlockViewAdapter);

		List<FlowModel> flowlist = new ArrayList<FlowModel>();
		FlowModel flowModel = null;
		for(int i = 0;i < strings.size();i++)
		{
			flowModel = new FlowModel(strings.get(i),false);
			flowlist.add(flowModel);
		}

		mFlowAdapter = new FlowAdapter(this);
		mFlowAdapter.setFlowModelList(flowlist);
		mFlowLayout.setAdapter(mFlowAdapter);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				showlables();
			}
		},1000);
	}
}
