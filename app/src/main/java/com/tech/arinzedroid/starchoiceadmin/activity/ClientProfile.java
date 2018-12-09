package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.adapter.UserProductAdapter;
import com.tech.arinzedroid.starchoiceadmin.fragment.ConfirmDialogFragment;
import com.tech.arinzedroid.starchoiceadmin.interfaces.UserProductClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.model.UserProductsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.utils.FormatUtil;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientProfile extends AppCompatActivity implements
        UserProductClickedInterface, ConfirmDialogFragment.OnButtonClickInterface {

    @BindView(R.id.name)
    TextView nameTv;
    @BindView(R.id.recycler_view)
    RecyclerView userProductRv;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    @BindView(R.id.refresh)
    Button refreshBtn;
    @BindView(R.id.phone)
    TextView phoneTv;
    @BindView(R.id.agent_name)
    TextView agentNameTv;
    @BindView(R.id.address)
    TextView addressTv;
    @BindView(R.id.kin_name)
    TextView kinNameTv;
    @BindView(R.id.kin_address)
    TextView kinAddressTv;
    @BindView(R.id.kin_phone)
    TextView kinPhoneTv;
    @BindView(R.id.amt_remaining)
    TextView amtRemainingTv;
    @BindView(R.id.amt_paid)
    TextView amtPaidTv;
    @BindView(R.id.product_bought)
    TextView productBoughtTv;

    private AppViewModel appViewModel;
    private UserProductAdapter userProductAdapter;
    private List<UserProductsModel> userProductsModelList;
    ClientsModel clientsModel;
    private int activityCode = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        ButterKnife.bind(this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Clients Profile");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        refreshBtn.setOnClickListener(view -> {
            getUserProducts(true);
        });
        findViewById(R.id.add).setOnClickListener(view -> {
            Intent intent = new Intent(this,AddProductActivity.class);
            intent.putExtra(Constants.CLIENT_DATA,Parcels.wrap(clientsModel));
            startActivityForResult(intent,activityCode);
        });

        getDataFromIntent(getIntent());
    }

    private void getDataFromIntent(Intent intent){
        if(intent != null){
            clientsModel = Parcels.unwrap(intent.getParcelableExtra(Constants.CLIENT_DATA));
            if(clientsModel != null){
                nameTv.setText(clientsModel.getFullname());
                phoneTv.setText(clientsModel.getPhone());
                addressTv.setText(clientsModel.getAddress());
                kinNameTv.setText(clientsModel.getKinName());
                kinAddressTv.setText(clientsModel.getKinAddress());
                kinPhoneTv.setText(clientsModel.getKinPhone());
                agentNameTv.setText(String.valueOf("Agent: " + clientsModel.getAgentName()));
                getUserProducts(false);
            }else{
                Toast.makeText(this, "Invalid client selected", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void computeTransValuesForUser(List<UserProductsModel> userProductsModels){
        if(userProductsModels != null && !userProductsModels.isEmpty()){
            int totalBought = 0; double totalAmt = 0, totalAmtPaid = 0, totalAmtRem = 0;
            for(int i = 0; i < userProductsModels.size(); i++){
                if(userProductsModels.get(i).isPaidFully())
                    totalBought += 1;
                totalAmt += userProductsModels.get(i).getProductModel().getPrice();
                totalAmtPaid += userProductsModels.get(i).getAmtPaid();
            }
            totalAmtRem = totalAmt - totalAmtPaid;
            amtPaidTv.setText(FormatUtil.formatPrice(totalAmtPaid));
            amtRemainingTv.setText(FormatUtil.formatPrice(totalAmtRem));
            productBoughtTv.setText(String.valueOf(totalBought));
        }else{
            amtPaidTv.setText(FormatUtil.formatPrice(0));
            amtRemainingTv.setText(FormatUtil.formatPrice(0));
            productBoughtTv.setText("0");
        }
    }

    private void getUserProducts(boolean refresh){
        progressBar.setVisibility(View.VISIBLE);
        if(userProductAdapter != null){
            userProductAdapter.clearData();
            this.userProductsModelList.clear();
        }

        appViewModel.getUserProducts(clientsModel.getId(),refresh).observe(this, userProductsModels -> {
            if(userProductsModels != null && !userProductsModels.isEmpty()){
                userProductAdapter = new UserProductAdapter(userProductsModels,this);
                userProductRv.setAdapter(userProductAdapter);
                this.userProductsModelList = new ArrayList<>(userProductsModels);
                computeTransValuesForUser(userProductsModels);
            }else
                computeTransValuesForUser(null);
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if(menuItem.getItemId() == R.id.edit_btn){
            Intent intent = new Intent(this, CreateClientActivity.class);
            intent.putExtra(Constants.CLIENT_DATA,Parcels.wrap(clientsModel));
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onProductClick(int position) {
        if(userProductsModelList != null && !userProductsModelList.isEmpty() &&
                userProductsModelList.size() > position){
            ConfirmDialogFragment fragment = ConfirmDialogFragment.FragmentInstance(position);
            fragment.show(getSupportFragmentManager(),"ConfirmDialog");
        }else{
            Toast.makeText(this, "Invalid product selected", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onDeleteClicked(int position) {
        userProductAdapter.removeItem(position);
        appViewModel.deleteUserProduct(userProductsModelList.get(position)).observe(this, isSuccessful -> {
            if(isSuccessful != null && isSuccessful){
                userProductsModelList.remove(position);
                computeTransValuesForUser(userProductsModelList);
                Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            }else{
                userProductAdapter.addProduct(position,userProductsModelList.get(position));
                Toast.makeText(this, "Product deletion failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewClicked(int position) {
        Intent intent = new Intent(this,TransactionActivity.class);
        intent.putExtra(Constants.IS_EDIT,true);
        intent.putExtra(Constants.PRODUCT_DATA, Parcels.wrap(userProductsModelList.get(position)));
        startActivity(intent);
    }
}
