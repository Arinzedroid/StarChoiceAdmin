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
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private AppViewModel appViewModel;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.username)
    EditText usernameEt;
    @BindView(R.id.password)
    EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Admin");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
    }

    private void validateControls(){


        if(TextUtils.isEmpty(usernameEt.getText())){
            usernameEt.setError("Invalid username");
            return;
        }
        if(TextUtils.isEmpty(passwordEt.getText())){
            passwordEt.setError("Invalid password");
            return;
        }
        String password = passwordEt.getText().toString();
        String username = usernameEt.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);
        appViewModel.loginAdmin(username,password).observe(this, adminModel -> {
            progressBar.setVisibility(View.GONE);
            if(adminModel != null){
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(Constants.ADMIN_NAME,adminModel.getFullname());
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                loginBtn.setEnabled(true);
            }
        });
    }

    @OnClick(R.id.login_btn)
    public void login(View v){
       validateControls();
    }
}
