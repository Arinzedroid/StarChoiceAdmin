package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tech.arinzedroid.starchoiceadmin.fragment.AddProductFragment;
import com.tech.arinzedroid.starchoiceadmin.fragment.SelectProductFragment;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;

public class AddProductSectionsPagerAdapter extends FragmentPagerAdapter {

    private ClientsModel clientsModel;

    public AddProductSectionsPagerAdapter(FragmentManager fm, ClientsModel clientsModel) {
        super(fm);
        this.clientsModel = clientsModel;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return AddProductFragment.newInstance(clientsModel);
        }else{
            return SelectProductFragment.newInstance(clientsModel);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
