package com.base.application.baseapplication.jncax.coordinatorlayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by chenaxing on 2016/12/21.
 */

public class MyViewPagerAdapter extends FragmentStatePagerAdapter
{

    private List<Fragment> fragmentList;

    public MyViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public void setFragmentList(List<Fragment> list)
    {
        fragmentList = list;
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
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

    @Override
    public int getCount()
    {
        return fragmentList.size();
    }
}
