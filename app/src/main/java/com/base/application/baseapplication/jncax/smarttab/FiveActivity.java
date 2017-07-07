package com.base.application.baseapplication.jncax.smarttab;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.application.baseapplication.R;
import com.base.application.baseapplication.jncax.coordinatorlayout.RecyclerViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryCaicos on 2016/12/20.
 */

public class FiveActivity extends AppCompatActivity
{
    protected TabLayout mTabLayout;
    private ViewPager mViewPager;

    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2, view3, view4, view5;//页卡视图
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acjn_activity_tablayout_viewpager);

        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        RecyclerViewFragment fragment1 = new RecyclerViewFragment();
        RecyclerViewFragment fragment2 = new RecyclerViewFragment();
        RecyclerViewFragment fragment3 = new RecyclerViewFragment();
        RecyclerViewFragment fragment4 = new RecyclerViewFragment();

        //        fragment1.setParams(0);
        //        fragment2.setParams(1);
        //        fragment3.setParams(2);
        //        fragment4.setParams(3);
        //
        //        fragment1.setContext(this);
        //        fragment2.setContext(this);
        //        fragment3.setContext(this);
        //        fragment4.setContext(this);

        mViewList.add(fragment1);
        mViewList.add(fragment2);
        mViewList.add(fragment3);
        mViewList.add(fragment4);

        //添加页卡标题
        mTitleList.add("No:1");
        mTitleList.add("No:2");
        mTitleList.add("No:3");
        mTitleList.add("No:4");


        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(3)));


        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mAdapter.setmViewList(mViewList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mViewPager.setCurrentItem(0);
        //        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        //        {
        //            @Override
        //            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        //            {
        //
        //            }
        //
        //            @Override
        //            public void onPageSelected(int position)
        //            {
        //
        //            }
        //
        //            @Override
        //            public void onPageScrollStateChanged(int state)
        //            {
        //
        //            }
        //        });
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
    }


    //ViewPager适配器
    class MyPagerAdapter extends FragmentStatePagerAdapter
    {
        private List<Fragment> mViewList;

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        public void setmViewList(List<Fragment> list)
        {
            mViewList = list;
        }

        @Override
        public int getCount()
        {
            return mViewList.size();//页卡数
        }


        @Override
        public Fragment getItem(int position)
        {
            return mViewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            String title = "FIRST";
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
    }

}