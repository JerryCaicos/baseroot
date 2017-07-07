package com.base.application.baseapplication.jncax.coordinatorlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.application.baseapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryCaicos on 2016/12/21.
 */

public class RecyclerViewFragment extends Fragment
{
    private View view;
    private TextView textView;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.acjn_activity_smart_tab_fragment, container, false);
        //        textView = (TextView) view.findViewById(R.id.textView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), getListData()));
        return view;
    }

    private List<String> getListData()
    {
        List<String> list = new ArrayList<>();
        String item = "1111111111111";
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
