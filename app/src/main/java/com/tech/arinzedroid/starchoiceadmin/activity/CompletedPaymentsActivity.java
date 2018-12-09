package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.adapter.CompletedProductsAdapter;
import com.tech.arinzedroid.starchoiceadmin.model.CompletedProducts;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedPaymentsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.search_et)
    EditText searchET;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AppViewModel appViewModel;
    private CompletedProductsAdapter completedProductsAdapter;
    private List<CompletedProducts> completedProducts;
    private List<CompletedProducts> searchedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_payments);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Completed Payments");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(view -> finish());

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        loadData(false);

        onSearchQuery();
    }

    private void loadData(boolean refresh){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getCompletedProducts(refresh).observe(this, completedProductsList -> {
            swipeRefreshLayout.setRefreshing(false);
            if(completedProductsList != null && !completedProductsList.isEmpty()){
                completedProducts = completedProductsList;
                searchedProducts = completedProductsList;
                completedProductsAdapter = new CompletedProductsAdapter(completedProductsList);
                recyclerView.setAdapter(completedProductsAdapter);
            }
        });
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

    private void onSearchQuery(){
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
        if(!TextUtils.isEmpty(query)){
            if(completedProducts != null && !completedProducts.isEmpty()){
                List<CompletedProducts> products = new ArrayList<>();
                for(int i = 0; i < completedProducts.size(); i++){
                    if(completedProducts.get(i) != null &&
                            completedProducts.get(i).getProductModel() != null &&
                            completedProducts.get(i).getProductModel().getProductName()
                                    .toLowerCase().contains(query)){
                        products.add(completedProducts.get(i));
                    }
                }
                searchedProducts = products;
                completedProductsAdapter = new CompletedProductsAdapter(products);
                recyclerView.setAdapter(completedProductsAdapter);
            }
        }else{
            searchedProducts = completedProducts;
            completedProductsAdapter.addAll(completedProducts);
        }
    }

    @Override
    public void finish(){
        if(searchET.hasFocus()){
            searchET.setText("");
            searchET.clearFocus();
            searchET.setVisibility(View.GONE);
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle("Completed Products");
            }
        }else{
            super.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.search){
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }
}
