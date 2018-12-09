package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.adapter.TransactionAdapter;
import com.tech.arinzedroid.starchoiceadmin.fragment.EditTransactionDialogFragment;
import com.tech.arinzedroid.starchoiceadmin.interfaces.TransactionItemClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.model.TransactionsModel;
import com.tech.arinzedroid.starchoiceadmin.model.UserProductsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, EditTransactionDialogFragment.OnButtonClickedInterface,
        TransactionItemClickedInterface {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AppViewModel appViewModel;
    private boolean isAllTransactions, isEdit;
    private UserProductsModel userProductsModel;
    private String agentId; private int position;
    private List<TransactionsModel> transactionsModelList;
    private TransactionAdapter adapter;

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

        getDataFromIntent(getIntent());
    }

    private void getDataFromIntent(Intent intent){
        if(intent != null){
            isEdit = intent.getBooleanExtra(Constants.IS_EDIT,false);
            if(isEdit){
                userProductsModel = Parcels.unwrap(intent.getParcelableExtra(Constants.PRODUCT_DATA));
                if(userProductsModel != null){
                    loadDataByProduct(userProductsModel.getProductId(),false);
                }else{
                    Toast.makeText(this, "Invalid product selected", Toast.LENGTH_SHORT).show();
                }
            }else{
                AgentsModel data = Parcels.unwrap(intent.getParcelableExtra(Constants.AGENT_DATA));
                if(data != null){
                    isAllTransactions = false;
                    agentId = data.getId();
                    loadData(agentId,false);
                }else{
                    isAllTransactions = true;
                    loadData(false);
                }
            }

        }else{

        }
    }

    private void loadData(boolean refresh){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAllTransactions(refresh).observe(this, data ->{
            swipeRefreshLayout.setRefreshing(false);
            if(data != null){
                transactionsModelList = data;
                adapter = new TransactionAdapter(data, this);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void loadData(String agentId, boolean refresh){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAgentTransactions(agentId,refresh).observe(this, data ->{
            swipeRefreshLayout.setRefreshing(false);
            if(data != null){
                transactionsModelList = data;
                adapter = new TransactionAdapter(data,this);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void loadDataByProduct(String productId, boolean refresh){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getUserTransactionByProduct(productId,refresh).observe(this,
                transactionsModels -> {
            swipeRefreshLayout.setRefreshing(false);
            if(transactionsModels != null && !transactionsModels.isEmpty()){
                transactionsModelList = transactionsModels;
                adapter = new TransactionAdapter(transactionsModels,this);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onRefresh() {
        if(isEdit){
            loadDataByProduct(userProductsModel.getProductId(),true);
        }else{
            if(isAllTransactions)
                loadData(true);
            else
                loadData(agentId,true);
        }
    }

    @Override
    public void onDeleteClicked(TransactionsModel transactionsModel) {
        if(adapter != null){
            adapter.deleteTransaction(position,transactionsModel);
        }
        appViewModel.deleteTransactionItem(transactionsModel).observe(this, isSuccessful -> {
            if(isSuccessful != null && isSuccessful){
                Toast.makeText(this, "Transaction has been deleted successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Deleting transaction failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveClicked(TransactionsModel transactionsModel) {
        if(adapter != null){
            adapter.updateTransaction(position,transactionsModel);
        }
        appViewModel.updateTransactionItem(transactionsModel).observe(this, isSuccessful -> {
            if(isSuccessful != null && isSuccessful){
                Toast.makeText(this, "Transaction has been updated successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Updating transaction failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void TransactionItem(int position) {
        this.position = position;
        if(transactionsModelList != null && !transactionsModelList.isEmpty() &&
                transactionsModelList.size() > position){
            EditTransactionDialogFragment frag = EditTransactionDialogFragment
                    .instance(transactionsModelList.get(position));

            frag.show(getSupportFragmentManager(),"Edit Fragment");
        }
    }
}
