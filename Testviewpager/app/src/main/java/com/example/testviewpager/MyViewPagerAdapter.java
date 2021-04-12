package com.example.testviewpager;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
    //存放Fragment的列表
    private final List<Fragment> myFragments = new ArrayList<>();
    //存放Fragment Title的列表
    private final List<String> myFragmentTitles = new ArrayList<>();
    private Context mcontext;


    public MyViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mcontext = context;
    }
    public void addFragment(Fragment fragment, String title) {
        myFragments.add(fragment);
        myFragmentTitles.add(title);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return myFragmentTitles.get(position);
    }
}
