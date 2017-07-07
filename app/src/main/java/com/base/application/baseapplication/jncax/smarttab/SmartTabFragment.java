package com.base.application.baseapplication.jncax.smarttab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.application.baseapplication.R;
import com.base.application.baseapplication.jncax.coordinatorlayout.RecyclerViewAdapter;
import com.base.application.baseapplication.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryCaicos on 2016/12/20.
 */

public class SmartTabFragment extends Fragment
{
    private RecyclerView recyclerView;
    private int position = 0;
    View view;
    private Context mContext;

    public void setContext(Context context)
    {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.acjn_activity_main, container, false);
        //        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_content);
        //
        //        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //        recyclerView.setAdapter(new RecyclerViewAdapter(mContext, getListData()));
        ToastUtils.showMessage(position+"");
        return view;
    }

    public CharSequence getTitle()
    {
        CharSequence title = "FIRST";
        switch(position)
        {
            case 0:
                title = "FIRST";
                break;
            case 1:
                title = "SECOND";
                break;
            case 2:
                title = "THIRD";
                break;
            case 3:
                title = "FOURTH";
                break;
        }
        return title;
    }


    public void setParams(int local)
    {
        position = local;
    }

    private List<String> getListData()
    {
        List<String> list = new ArrayList<>();
        String item = "1111111111111";
        switch(position)
        {
            case 0:
                item = "1111111111111";
                break;
            case 1:
                item = "2222222222222";
                break;
            case 2:
                item = "3333333333333";
                break;
            case 3:
                item = "44444444444444";
                break;
        }
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        list.add(item);
        return list;
    }
}
