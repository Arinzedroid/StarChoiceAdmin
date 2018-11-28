package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tech.arinzedroid.starchoiceadmin.fragment.AgentsFragment;
import com.tech.arinzedroid.starchoiceadmin.fragment.ProductsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private String adminName;
    public SectionsPagerAdapter(String adminName,FragmentManager fm) {
        super(fm);
        this.adminName = adminName;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return AgentsFragment.newInstance(adminName);
        }else{
            return ProductsFragment.newInstance(adminName);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}