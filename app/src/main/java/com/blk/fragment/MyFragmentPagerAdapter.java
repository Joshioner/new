package com.blk.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment>fragList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragList) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.fragList=fragList;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return fragList.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragList.size();
    }



}
