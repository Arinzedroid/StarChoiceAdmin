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
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateClientActivity extends AppCompatActivity {

    @BindView(R.id.name)
    EditText nameTv;
    @BindView(R.id.address)
    EditText addressEt;
    @BindView(R.id.phone)
    EditText phoneEt;
    @BindView(R.id.kin_name)
    EditText kinName;
    @BindView(R.id.kin_address)
    EditText kinAddress;
    @BindView(R.id.kin_phone)
    EditText kinPhone;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.agent_name)
    EditText agentNameTv;

    ClientsModel clientsModel; boolean isEdit;
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_client);
        ButterKnife.bind(this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        getDataFromIntent(getIntent());
    }

    private void getDataFromIntent(Intent intent){
        if(intent != null){
            clientsModel = Parcels.unwrap(intent.getParcelableExtra(Constants.CLIENT_DATA));
            if(null == clientsModel){
                Toast.makeText(this, "Client data is invalid", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                nameTv.setText(clientsModel.getFullname());
                addressEt.setText(clientsModel.getAddress());
                phoneEt.setText(clientsModel.getPhone());
                kinName.setText(clientsModel.getKinName());
                kinAddress.setText(clientsModel.getKinAddress());
                kinPhone.setText(clientsModel.getKinPhone());
                agentNameTv.setText(clientsModel.getAgentName());
                registerBtn.setText("Save");
                isEdit = true;
            }
        }
    }


    @OnClick(R.id.register_btn)
    public void onRegister(View v){
        validateData(isEdit);
    }

    private void validateData(boolean isEdit){
        if(TextUtils.isEmpty(nameTv.getText())){
            nameTv.setError("Required");
            return;
        }
        if(TextUtils.isEmpty(phoneEt.getText())){
            phoneEt.setError("Required");
            return;
        }

        if(TextUtils.isEmpty(kinName.getText())){
            kinName.setError("Required");
            return;
        }
        if(TextUtils.isEmpty(kinPhone.getText())){
            kinPhone.setError("Required");
            return;
        }

        processData(isEdit);
    }

    private void processData(boolean isEdit){
        if(isEdit){
            progressBar.setVisibility(View.VISIBLE);
            registerBtn.setEnabled(false);
            String name = nameTv.getText().toString();
            String address = addressEt.getText().toString();
            String phone = phoneEt.getText().toString();

            String kName = kinName.getText().toString();
            String kAddress = kinAddress.getText().toString();
            String kPhone = kinPhone.getText().toString();

            ClientsModel data = clientsModel;
            data.setFullname(name);
            data.setAddress(address);
            data.setPhone(phone);
            data.setDateUpdated(new Date());
            data.setKinName(kName);
            data.setKinAddress(kAddress);
            data.setKinPhone(kPhone);

            appViewModel.updateClient(data).observe(this, isSuccessful -> {
                progressBar.setVisibility(View.GONE);
                registerBtn.setEnabled(true);
                if(isSuccessful != null && isSuccessful){
                    Toast.makeText(this, "Client data saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this, "Saving client data failed.", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }
}
