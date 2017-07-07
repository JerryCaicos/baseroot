package com.base.application.baseapplication.jncax.smarttab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.application.baseapplication.R;

/**
 * Created by JerryCaicos on 2016/12/20.
 */

public class SmartTabItemView extends LinearLayout
{

    private Context mContext;
    private LayoutInflater inflater;

    private RelativeLayout layoutTab;
    private TextView viewTabName;
    private ImageView viewTabUnderImg;

    public SmartTabItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mContext = context;
        inflater = LayoutInflater.from(mContext);
        addView(initView());
    }

    public void setViewTabName(String title)
    {
        viewTabName.setText(title);
    }

    private View initView()
    {
        View view = inflater.inflate(R.layout.acjn_layout_smart_tab_item_view, null);

        layoutTab = (RelativeLayout) view.findViewById(R.id.layout_jshop_recommend_tab);
        viewTabName = (TextView) view.findViewById(R.id.view_jshop_recommend_tab_name);
        viewTabUnderImg = (ImageView) view.findViewById(R.id.view_jshop_recommend_tab_under_img);
        return view;
    }

    @Override
    public void setSelected(boolean selected)
    {
        super.setSelected(selected);
        viewTabUnderImg.setVisibility(selected ? VISIBLE : GONE);
    }
}
