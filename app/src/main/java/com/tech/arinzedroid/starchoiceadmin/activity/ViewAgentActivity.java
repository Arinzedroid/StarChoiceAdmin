package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewAgentActivity extends AppCompatActivity {

    @BindView(R.id.age)
    TextView ageTv;
    @BindView(R.id.phone)
    TextView phoneTv;
    @BindView(R.id.address)
    TextView addressTv;
    @BindView(R.id.kin_name)
    TextView kinNameTv;
    @BindView(R.id.kin_phone)
    TextView kinPhoneTv;
    @BindView(R.id.kin_address)
    TextView kinAddress;
    @BindView(R.id.username)
    TextView usernameTv;
    @BindView(R.id.password)
    TextView passwordTv;
    @BindView(R.id.name)
    TextView nameTv;
    @BindView(R.id.edit_btn)
    Button editBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private AgentsModel agentsModel;
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_agent);
        ButterKnife.bind(this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Agent Profile");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        initViews(getIntent());
    }

    @OnClick(R.id.edit_btn)
    public void onEditClick(View view){
        Intent intent = new Intent(this,RegisterAgentActivity.class);
        intent.putExtra(Constants.AGENT_DATA,Parcels.wrap(agentsModel));
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.view_btn)
    public void onViewClick(View view){
        Intent intent = new Intent(this,TransactionActivity.class);
        intent.putExtra(Constants.AGENT_DATA,Parcels.wrap(agentsModel));
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.view_clients_btn)
    public void onViewClientClick(View view){
        Intent intent = new Intent(this,ViewClientsActivity.class);
        intent.putExtra(Constants.AGENT_DATA,Parcels.wrap(agentsModel));
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.delete_btn)
    public void onDeleteClick(){
        if(agentsModel != null) {
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.delete_btn).setEnabled(false);
            appViewModel.deleteAgent(agentsModel).observe(this, isSuccessful -> {
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.delete_btn).setEnabled(true);
                if(isSuccessful != null && isSuccessful){
                    Toast.makeText(this, "Agent account deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else
                    Toast.makeText(this, "Deleting account failed", Toast.LENGTH_SHORT).show();
            });
        }
        else
            Toast.makeText(this, "Error occurred. Please try again later", Toast.LENGTH_SHORT).show();
    }

    private void initViews(Intent intent){
        if(intent != null){
            agentsModel = Parcels.unwrap(intent.getParcelableExtra(Constants.AGENT_DATA));
            if(agentsModel != null){
                ageTv.setText(String.valueOf(agentsModel.getAge() + " yrs"));
                phoneTv.setText(agentsModel.getPhone());
                addressTv.setText(agentsModel.getAddress());
                kinNameTv.setText(agentsModel.getKinFulname());
                kinAddress.setText(agentsModel.getKinAddress());
                kinPhoneTv.setText(agentsModel.getKinPhone());
                nameTv.setText(String.valueOf(agentsModel.getFirstname() + " " + agentsModel.getLastname()));
                usernameTv.setText(agentsModel.getUsername());
                passwordTv.setText(agentsModel.getPassword());
                editBtn.setEnabled(true);
                findViewById(R.id.delete_btn).setEnabled(true);
                findViewById(R.id.view_btn).setEnabled(true);
                findViewById(R.id.view_clients_btn).setEnabled(true);
            }else
                Toast.makeText(this, "Error occurred. Try again later", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "Error occurred. Try again later", Toast.LENGTH_SHORT).show();
    }
}
