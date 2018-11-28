package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.adapter.ClientsAdapter;
import com.tech.arinzedroid.starchoiceadmin.interfaces.ClientItemClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.interfaces.DeleteClientInterface;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewClientsActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, DeleteClientInterface,ClientItemClickedInterface{

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AppViewModel appViewModel;
    private ClientsAdapter clientsAdapter;
    private List<ClientsModel> clientsModelList;
    private AgentsModel agentsModel;
    private boolean isAllClients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clients);
        ButterKnife.bind(this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Clients");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        getDataFromIntent(getIntent());
    }

    private void getDataFromIntent(Intent intent){
        if(intent != null){
            agentsModel = Parcels.unwrap(intent.getParcelableExtra(Constants.AGENT_DATA));
            if(agentsModel != null){
                isAllClients = false;
                loadData(agentsModel.getId());
            }else {
                isAllClients = true;
                loadData();
            }
        }else {
            isAllClients = true;
            loadData();
        }
    }

    private void loadData(){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAllClients().observe(this, clientsModels -> {
            swipeRefreshLayout.setRefreshing(false);
            if(clientsModels != null){
                this.clientsModelList = clientsModels;
                clientsAdapter = new ClientsAdapter(clientsModels,this,this);
                recyclerView.setAdapter(clientsAdapter);
            }else {
                Toast.makeText(this, "Error fetching clients. Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData(String agentId){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAgentsClients(agentId).observe(this, clientsModels -> {
            swipeRefreshLayout.setRefreshing(false);
            if(clientsModels != null){
                this.clientsModelList = clientsModels;
                clientsAdapter = new ClientsAdapter(clientsModels,this,this);
                recyclerView.setAdapter(clientsAdapter);
            }else
                Toast.makeText(this, "Error fetching clients. Try again later", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private void refreshData() {
        if(isAllClients)
            loadData();
        else loadData(agentsModel.getId());
    }

    @Override
    public void onDelete(int position) {
        Toast.makeText(this, "delete item at " + position + " selected", Toast.LENGTH_SHORT).show();
//        if(clientsModelList != null && !clientsModelList.isEmpty() && clientsModelList.size() > position){
//            appViewModel.deleteClients(clientsModelList.get(position)).observe(this, isSuccessful -> {
//                if(isSuccessful != null && isSuccessful) {
//                    Toast.makeText(this, "Client deleted successfully", Toast.LENGTH_SHORT).show();
//                    refreshData();
//                }
//                else
//                    Toast.makeText(this, "Client deleting failed", Toast.LENGTH_SHORT).show();
//            });
//        }else {
//            Toast.makeText(this, "Error deleting client. Try again later", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void client(int position) {
        Toast.makeText(this, "client item at " + position + " selected", Toast.LENGTH_SHORT).show();
    }
}
