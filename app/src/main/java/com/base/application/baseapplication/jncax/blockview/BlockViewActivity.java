package com.base.application.baseapplication.jncax.blockview;

import android.app.Activity;
import android.os.Bundle;


import com.base.application.baseapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryCaicos on 16/1/20 10:56.
 */
public class BlockViewActivity extends Activity
{
	private BlockView mBlockView;

	private BlockViewAdapter mBlockViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acjn_activity_block_view);

		mBlockView = (BlockView)findViewById(R.id.block_view);
		String str = "标签";
		String str1 = "标签标签";
		String str2 = "标签标签标签";
		String str3 = "标签标签标签标签";
		String str4 = "标签标签";
		String str5 = "标签标签";
		String str6 = "标签";
		String str7 = "标签标签标签标签";
		String str8 = "标签标签标签标签标签标签";
		String str9 = "标签标签标签";
		List<String> strings = new ArrayList<>();
		strings.add(str);
		strings.add(str1);
		strings.add(str2);
		strings.add(str3);
		strings.add(str4);
		strings.add(str5);
		strings.add(str6);
		strings.add(str7);
		strings.add(str8);
		strings.add(str9);
		List<BlockModel> lable = new ArrayList<BlockModel>();
		BlockModel blockModel = null;
		for(int i = 0;i < 10;i++)
		{
			blockModel = new BlockModel(strings.get(i),false);
			lable.add(blockModel);
		}

		mBlockViewAdapter = new BlockViewAdapter(this);
		mBlockViewAdapter.setLableList(lable);
		mBlockView.setAdapter(mBlockViewAdapter);
	}


}
