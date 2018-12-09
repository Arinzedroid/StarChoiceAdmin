package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewClientsActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, DeleteClientInterface,ClientItemClickedInterface{

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_et)
    EditText searchET;

    private AppViewModel appViewModel;
    private ClientsAdapter clientsAdapter;
    private List<ClientsModel> clientsModelList;
    private List<ClientsModel> searchedClientResult;
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

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(view -> finish());
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("View Clients");
        }

        onSearchQuery();

        getDataFromIntent(getIntent());
    }

    private void getDataFromIntent(Intent intent){
        if(intent != null){
            agentsModel = Parcels.unwrap(intent.getParcelableExtra(Constants.AGENT_DATA));
            if(agentsModel != null){
                isAllClients = false;
                loadData(agentsModel.getId(),false);
            }else {
                isAllClients = true;
                loadData(false);
            }
        }else {
            isAllClients = true;
            loadData(false);
        }
    }

    private void loadData(boolean refresh){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAllClients(refresh).observe(this, clientsModels -> {
            swipeRefreshLayout.setRefreshing(false);
            if(clientsModels != null){
                this.clientsModelList = clientsModels;
                searchedClientResult = clientsModels;
                clientsAdapter = new ClientsAdapter(clientsModels,this,this);
                recyclerView.setAdapter(clientsAdapter);
                Log.e(this.getClass().getSimpleName(),"clientsModelList size " + clientsModelList.size());
            }else {
                Toast.makeText(this, "Error fetching clients. Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData(String agentId, boolean refresh){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAgentsClients(agentId,refresh).observe(this, clientsModels -> {
            swipeRefreshLayout.setRefreshing(false);
            if(clientsModels != null){
                this.clientsModelList = clientsModels;
                searchedClientResult = clientsModels;
                clientsAdapter = new ClientsAdapter(clientsModels,this,this);
                recyclerView.setAdapter(clientsAdapter);
                Log.e(this.getClass().getSimpleName(),"clientsModelList size " + clientsModelList.size());
                //onSearchQuery();
            }else
                Toast.makeText(this, "Error fetching clients. Try again later", Toast.LENGTH_SHORT).show();
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
            if(clientsModelList != null && !clientsModelList.isEmpty()){
                List<ClientsModel> clientsModels = new ArrayList<>();
                for(ClientsModel clients : clientsModelList){
                    if(clients.getFullname().toLowerCase().contains(query.toLowerCase()) ||
                           clients.getKinName().toLowerCase().contains(query.toLowerCase())){
                        clientsModels.add(clients);
                    }
                }
                searchedClientResult = clientsModels;
                clientsAdapter = new ClientsAdapter(clientsModels,this,this);
                recyclerView.setAdapter(clientsAdapter);
            }
        }else{
            searchedClientResult = clientsModelList;
            clientsAdapter.addAll(clientsModelList);
        }
    }

    @Override
    public void finish(){
        if(searchET.hasFocus()){
            searchET.setText("");
            searchET.clearFocus();
            searchET.setVisibility(View.GONE);
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle("View Clients");
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
        refreshData();
    }

    private void refreshData() {
        if(isAllClients)
            loadData(true);
        else loadData(agentsModel.getId(),true);
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
        if(searchedClientResult != null && !searchedClientResult.isEmpty() && searchedClientResult.size() > position){
            Intent intent = new Intent(this,ClientProfile.class);
            intent.putExtra(Constants.CLIENT_DATA,Parcels.wrap(searchedClientResult.get(position)));
            startActivity(intent);
        }
    }
}
