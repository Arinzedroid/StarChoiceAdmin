package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.adapter.AddProductSectionsPagerAdapter;
import com.tech.arinzedroid.starchoiceadmin.fragment.AddProductFragment;
import com.tech.arinzedroid.starchoiceadmin.fragment.SelectProductFragment;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.model.UserProductsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.List;

public class AddProductActivity extends AppCompatActivity implements
        SelectProductFragment.OnRegisterInterface, AddProductFragment.OnRegisterProduct {

    private ClientsModel clientsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get data from intent and parse into clientModel. This method must be called before
        //creating sectionsPagerAdapter
        getDataFromIntent(getIntent());

        AddProductSectionsPagerAdapter mSectionsPagerAdapter =
                new AddProductSectionsPagerAdapter(getSupportFragmentManager(), clientsModel);

        AppViewModel appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        //get products from server
        appViewModel.getAllProducts(false);

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));




    }

    private void getDataFromIntent(Intent intent){
        if(intent != null){
            clientsModel = Parcels.unwrap(intent.getParcelableExtra(Constants.CLIENT_DATA));
            if(clientsModel == null){
                Toast.makeText(this, "Invalid client data. Please try again.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            Toast.makeText(this, "Invalid client data. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRegister(List<UserProductsModel> userProductModels) {

    }

    @Override
    public void onRegister(ProductsModel productsModel) {

    }
}
