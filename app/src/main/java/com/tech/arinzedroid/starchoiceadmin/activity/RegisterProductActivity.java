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
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterProductActivity extends AppCompatActivity {

    @BindView(R.id.product_name)
    EditText productNameEt;
    @BindView(R.id.product_price)
    EditText productPriceEt;
    @BindView(R.id.product_desc)
    EditText productDescEt;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private AppViewModel appViewModel;
    private boolean isEdit;
    private ProductsModel productsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);
        ButterKnife.bind(this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Create Product");
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        init(getIntent());
    }

    private void init(Intent intent){
        if(intent != null){
            productsModel = Parcels.unwrap(intent.getParcelableExtra(Constants.PRODUCT_DATA));
            if(productsModel != null){
                productNameEt.setText(productsModel.getProductName());
                productPriceEt.setText(String.valueOf(productsModel.getPrice()));
                productDescEt.setText(productsModel.getDesc());
                registerBtn.setText(String.valueOf("Update"));
                isEdit = true;
                if(getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Edit Product");
            }else
                isEdit = false;
        }else
            isEdit = false;
    }

    @OnClick(R.id.register_btn)
    public void onRegisterClick(View view){
        validateData(isEdit);
    }

    @OnClick(R.id.delete_btn)
    public void onDeleteClick(View view){
        registerBtn.setEnabled(false);
        findViewById(R.id.delete_btn).setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        appViewModel.deleteProduct(productsModel).observe(this, isSuccessful -> {

            registerBtn.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            findViewById(R.id.delete_btn).setEnabled(true);

            if(isSuccessful != null && isSuccessful){
                Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }else
                Toast.makeText(this, "Product deleting failed", Toast.LENGTH_SHORT).show();
        });
    }

    private void validateData(boolean isEdit){
        if(TextUtils.isEmpty(productNameEt.getText())){
            productNameEt.setError("Invalid product name");
            return;
        }
        if(TextUtils.isEmpty(productPriceEt.getText())){
            productPriceEt.setError("Invalid product price");
            return;
        }

        processData(isEdit);
    }

    private void processData(boolean isEdit){
        registerBtn.setEnabled(false);
        findViewById(R.id.delete_btn).setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        String name = productNameEt.getText().toString();
        String desc = productDescEt.getText().toString();
        double price = Double.parseDouble(productPriceEt.getText().toString());

        ProductsModel productsModel = new ProductsModel();
        productsModel.setProductName(name);
        productsModel.setPrice(price);
        productsModel.setDesc(desc);
        productsModel.setActive(true);

        if(isEdit){
            if(this.productsModel != null)
                productsModel.setId(this.productsModel.getId());
            appViewModel.updateProduct(productsModel).observe(this, isSuccessful -> {

                registerBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.delete_btn).setEnabled(true);

                if(isSuccessful != null && isSuccessful){
                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this, "Product updating failed", Toast.LENGTH_SHORT).show();
            });
        }else{
            appViewModel.addProducts(productsModel).observe(this, isSuccessful -> {
                registerBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.delete_btn).setEnabled(true);

                if(isSuccessful != null && isSuccessful){
                    clearViews();
                    Toast.makeText(this, "New product created successfully", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "Creating new product failed", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void clearViews(){
        productNameEt.setText("");
        productPriceEt.setText("");
        productDescEt.setText("");
        productNameEt.requestFocus();
    }
}
