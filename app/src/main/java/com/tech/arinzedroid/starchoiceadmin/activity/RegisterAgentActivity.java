package com.tech.arinzedroid.starchoiceadmin.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterAgentActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText usernameEt;
    @BindView(R.id.password)
    EditText passwordEt;
    @BindView(R.id.firstname)
    EditText firstnameEt;
    @BindView(R.id.lastname)
    EditText lastnameEt;
    @BindView(R.id.age)
    EditText ageEt;
    @BindView(R.id.phone)
    EditText phoneEt;
    @BindView(R.id.address)
    EditText addressEt;
    @BindView(R.id.kin_name)
    EditText kinNameEt;
    @BindView(R.id.kin_phone)
    EditText kinPhoneEt;
    @BindView(R.id.kin_address)
    EditText kinAddressEt;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private AgentsModel agentsModel;
    private AppViewModel appViewModel;
    boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_agent);
        ButterKnife.bind(this);

        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Create Agent");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        initViews(getIntent());
    }

    @OnClick(R.id.register_btn)
    public void onRegisterClick(View view){
        validateData(isEdit);
    }

    private void initViews(Intent intent){
        if(intent != null){
            agentsModel = Parcels.unwrap(intent.getParcelableExtra(Constants.AGENT_DATA));
            if(agentsModel != null){
                usernameEt.setText(agentsModel.getUsername());
                passwordEt.setText(agentsModel.getPassword());
                firstnameEt.setText(agentsModel.getFirstname());
                lastnameEt.setText(agentsModel.getLastname());
                ageEt.setText(String.valueOf(agentsModel.getAge()));
                phoneEt.setText(agentsModel.getPhone());
                addressEt.setText(agentsModel.getAddress());
                kinNameEt.setText(agentsModel.getKinFulname());
                kinPhoneEt.setText(agentsModel.getKinPhone());
                kinAddressEt.setText(agentsModel.getKinAddress());
                isEdit = true;
                registerBtn.setText(String.valueOf("Update account"));
                if(getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Edit Agent");
            }
        }
    }

    private void validateData(boolean isEdit){
       if(TextUtils.isEmpty(usernameEt.getText()) || usernameEt.getText().length() < 6){
           usernameEt.setError("Invalid username. Must be above 5 characters");
           return;
       }
       if(TextUtils.isEmpty(passwordEt.getText()) || passwordEt.getText().length() < 6){
            passwordEt.setError("Invalid password. Must be above 5 characters");
            return;
       }
        if(TextUtils.isEmpty(firstnameEt.getText())){
            firstnameEt.setError("Invalid username");
            return;
        }
        if(TextUtils.isEmpty(lastnameEt.getText())){
            lastnameEt.setError("Invalid username");
            return;
        }
        if(TextUtils.isEmpty(ageEt.getText()) || ageEt.getText().length() > 2){
           ageEt.setError("Invalid age");
           return;
        }
        if(TextUtils.isEmpty(phoneEt.getText()) || phoneEt.getText().length() < 9){
           phoneEt.setError("Invalid phone");
           return;
        }

        processData(isEdit);
    }

    private void processData(boolean isEdit){

        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);

        String username = usernameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String firstname = firstnameEt.getText().toString();
        String lastname = lastnameEt.getText().toString();
        String phone = phoneEt.getText().toString();
        int age = Integer.parseInt(ageEt.getText().toString());
        String address = addressEt.getText().toString();
        String kinName = kinNameEt.getText().toString();
        String kinPhone = kinPhoneEt.getText().toString();
        String kinAddress = kinAddressEt.getText().toString();

        AgentsModel agentsModel = new AgentsModel();
        agentsModel.setUsername(username);
        agentsModel.setPassword(password);
        agentsModel.setFirstname(firstname);
        agentsModel.setLastname(lastname);
        agentsModel.setPhone(phone);
        agentsModel.setAge(age);
        agentsModel.setAddress(address);
        agentsModel.setKinFulname(kinName);
        agentsModel.setKinPhone(kinPhone);
        agentsModel.setKinAddress(kinAddress);

        if(isEdit){
            if(this.agentsModel != null)
                agentsModel.setId(this.agentsModel.getId());
            appViewModel.updateAgent(agentsModel).observe(this, isSuccessful -> {
                progressBar.setVisibility(View.GONE);
                registerBtn.setEnabled(true);
                if(isSuccessful != null && isSuccessful){
                    Toast.makeText(this, "Agent account updated successfully", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this, "Account updating failed", Toast.LENGTH_SHORT).show();
            });
        }else{
            appViewModel.addAgents(agentsModel).observe(this, isSuccessful -> {
                progressBar.setVisibility(View.GONE);
                registerBtn.setEnabled(true);
                if(isSuccessful != null && isSuccessful){
                    clearViews();
                    Toast.makeText(this, "New Agent created successfully", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(this, "Creating agent failed. Please try again later",
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void clearViews(){
        usernameEt.setText("");
        passwordEt.setText("");
        firstnameEt.setText("");
        lastnameEt.setText("");
        phoneEt.setText("");
        ageEt.setText("");
        addressEt.setText("");
        kinNameEt.setText("");
        kinAddressEt.setText("");
        kinPhoneEt.setText("");
        usernameEt.requestFocus();
    }
}
