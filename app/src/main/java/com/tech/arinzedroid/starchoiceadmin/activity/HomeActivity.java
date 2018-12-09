package com.tech.arinzedroid.starchoiceadmin.activity;


import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.adapter.SectionsPagerAdapter;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;


public class HomeActivity extends AppCompatActivity {

    EditText searchET;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchET = findViewById(R.id.search_et);

        handleEditText();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        String adminName = "";
        if(getIntent() != null){
            adminName = getIntent().getStringExtra(Constants.ADMIN_NAME);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(adminName,getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private void toggleSoftKeyPad(boolean show){
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            if(show){
                inputMethodManager.showSoftInput(searchET,InputMethodManager.SHOW_IMPLICIT);
            }else{
                inputMethodManager.hideSoftInputFromWindow(searchET.getWindowToken(),0);
            }

        }
    }

    private void handleEditText(){
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void performSearch(String query){
        appViewModel.setQuery(query);
    }

    @Override
    public void onBackPressed(){
        if(searchET.hasFocus()){
            searchET.setText("");
            searchET.clearFocus();
            searchET.setVisibility(View.GONE);
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle("Home");
            }
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(),SearchActivity.class)));
//        searchView.setIconifiedByDefault(true);
//        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.view_transactions:{
                startActivity(new Intent(this,TransactionActivity.class));
                return true;
            }case R.id.view_clients:{
                startActivity(new Intent(this,ViewClientsActivity.class));
                return true;
            }case R.id.view_completed_products:{
                startActivity(new Intent(this,CompletedPaymentsActivity.class));
                return true;
            }case R.id.search:{
                if(getSupportActionBar() != null){
                    getSupportActionBar().setTitle("");
                    if(searchET.getVisibility() == View.GONE){
                        searchET.setVisibility(View.VISIBLE);
                        searchET.requestFocus();
                        toggleSoftKeyPad(true);
                    }
                }
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
