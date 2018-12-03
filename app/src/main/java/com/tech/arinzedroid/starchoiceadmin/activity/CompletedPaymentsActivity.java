package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.adapter.CompletedProductsAdapter;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedPaymentsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AppViewModel appViewModel;
    private CompletedProductsAdapter completedProductsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_payments);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Completed Payments");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        loadData(false);
    }

    private void loadData(boolean refresh){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getCompletedProducts(refresh).observe(this, completedProductsList -> {
            swipeRefreshLayout.setRefreshing(false);
            if(completedProductsList != null && !completedProductsList.isEmpty()){
                completedProductsAdapter = new CompletedProductsAdapter(completedProductsList);
                recyclerView.setAdapter(completedProductsAdapter);
            }
        });
    }


    @Override
    public void onRefresh() {
        loadData(true);
    }
}
