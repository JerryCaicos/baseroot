package com.base.application.baseapplication.jncax.coordinatorlayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.base.application.baseapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryCaicos on 2016/12/22.
 */

public class ScrollViewPagerActivity extends AppCompatActivity
{
    private ViewPager mVpBody;
    List<Fragment> fragments;
    private int currIndex;
    List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acjn_activity_scroll_viewpager);
        initViewPager();
    }

    private void initViewPager()
    {
        try
        {
            TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
            titles = new ArrayList<>();
            titles.add("Exchange");
            titles.add("Activity");
            titles.add("Me");

            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));

            mTabLayout.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.red_ball));

            mVpBody = (ViewPager) findViewById(R.id.vp_body);
            fragments = new ArrayList<Fragment>();
            Bundle bundle = new Bundle();
            fragments.add(new RecyclerViewFragment());
            fragments.add(new RecyclerViewFragment());
            fragments.add(new RecyclerViewFragment());
            TabFragmentPagerAdapter tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragments);
            mVpBody.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), fragments));
            mVpBody.setCurrentItem(0);
            mVpBody.setOnPageChangeListener(new MyOnPageChangeListener());


            mTabLayout.setupWithViewPager(mVpBody);
            mTabLayout.setTabsFromPagerAdapter(tabFragmentPagerAdapter);

        }
        catch(Exception e)
        {
            Log.e("initViewPager", "initViewPager", e);
        }

    }

    public class TabFragmentPagerAdapter extends FragmentPagerAdapter
    {
        List<Fragment> mFragmentsList;

        public TabFragmentPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentsList)
        {
            super(fm);
            mFragmentsList = fragmentsList;
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return titles.get(position);
        }
    }

    public class TabOnClickListener implements View.OnClickListener
    {
        private int index = 0;

        public TabOnClickListener(int i)
        {
            index = i;
        }

        @Override
        public void onClick(View v)
        {
            mVpBody.setCurrentItem(index);
        }
    }

    ;

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
    {

        @Override
        public void onPageSelected(int arg0)
        {

            switch(arg0)
            {
                case 0:


                    break;
                case 1:

                    break;
                case 2:

                    break;

            }
            currIndex = arg0;

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
        }

        @Override
        public void onPageScrollStateChanged(int arg0)
        {
        }
    }
}
