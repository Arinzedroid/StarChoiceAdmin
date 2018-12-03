package com.tech.arinzedroid.starchoiceadmin.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.model.UserProductsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddProductFragment extends Fragment {

    private ClientsModel clientsModel;
    private OnRegisterProduct onRegisterProduct;

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

    public AddProductFragment() {
        // Required empty public constructor
    }

    public static AddProductFragment newInstance(ClientsModel clientsModel) {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.CLIENT_DATA, Parcels.wrap(clientsModel));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientsModel = Parcels.unwrap(getArguments().getParcelable(Constants.CLIENT_DATA));
        }
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_product,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    private void validateData(){
        if(TextUtils.isEmpty(productNameEt.getText())){
            productNameEt.setError("Product Name must be specified");
            return;
        }
        if(TextUtils.isEmpty(productPriceEt.getText())){
            productPriceEt.setError("Product Price must be specified");
            return;
        }

        registerBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        String productName = productNameEt.getText().toString();
        double productPrice = Double.parseDouble(productPriceEt.getText().toString());
        String productDesc = productDescEt.getText().toString();
        ProductsModel productsModel = new ProductsModel();
        productsModel.setDesc(productDesc);
        productsModel.setActive(true);
        productsModel.setPrice(productPrice);
        productsModel.setProductName(productName);
        //onRegisterProduct.onRegister(productsModel);

        appViewModel.addProducts(productsModel).observe(this, _isSuccessful -> {
            if(_isSuccessful != null && _isSuccessful){

                //product was created successfully proceed to adding it to client acct
                List<UserProductsModel> userProductsModelList = new ArrayList<>();

                UserProductsModel userProductsModel = new UserProductsModel();
                userProductsModel.setActive(true);
                userProductsModel.setProductModel(productsModel);
                userProductsModel.setUserModel(clientsModel);
                userProductsModel.setUserId(clientsModel.getId());
                userProductsModel.setProductId(productsModel.getId());

                userProductsModelList.add(userProductsModel);

                //add data to firebase server
                appViewModel.addUserProducts(userProductsModelList).observe(this, isSuccessful -> {
                    if(isSuccessful != null && isSuccessful){
                        Toast.makeText(getActivity(), "Products added to clients acct successfully", Toast.LENGTH_SHORT).show();
                        if(getActivity() != null){
                            getActivity().finish();
                        }
                    }else{
                        progressBar.setVisibility(View.GONE);
                        registerBtn.setEnabled(true);
                        Toast.makeText(getActivity(), "Product could not be added to this client. " +
                                "Please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                progressBar.setVisibility(View.GONE);
                registerBtn.setEnabled(true);
                Toast.makeText(getActivity(), "Product could not be created. " +
                        "Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.register_btn)
    public void onRegisterClicked(View v){
        validateData();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterProduct) {
            onRegisterProduct = (OnRegisterProduct) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRegisterProduct = null;
    }


    public interface OnRegisterProduct {
        void onRegister(ProductsModel productsModel);
    }
}
