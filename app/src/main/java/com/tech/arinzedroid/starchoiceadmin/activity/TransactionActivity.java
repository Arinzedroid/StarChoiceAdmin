package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.adapter.TransactionAdapter;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AppViewModel appViewModel;
    private boolean isAllTransactions;
    private String agentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Transactions");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        getAgentData(getIntent());
    }

    private void getAgentData(Intent intent){
        if(intent != null){
            AgentsModel data = Parcels.unwrap(intent.getParcelableExtra(Constants.AGENT_DATA));
            if(data != null){
                isAllTransactions = false;
                agentId = data.getId();
                loadData(agentId);
            }else{
                isAllTransactions = true;
                loadData();
            }
        }else{
            isAllTransactions = true;
            loadData();
        }
    }

    private void loadData(){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAllTransactions().observe(this, data ->{
            swipeRefreshLayout.setRefreshing(false);
            if(data != null){
                TransactionAdapter adapter = new TransactionAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });
    }
    private void loadData(String agentId){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAgentTransactions(agentId).observe(this, data ->{
            swipeRefreshLayout.setRefreshing(false);
            if(data != null){
                TransactionAdapter adapter = new TransactionAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onRefresh() {
        if(isAllTransactions)
            loadData();
        else
            loadData(agentId);
    }
}
