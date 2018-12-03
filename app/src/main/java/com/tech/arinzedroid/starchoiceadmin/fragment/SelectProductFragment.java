package com.tech.arinzedroid.starchoiceadmin.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.adapter.ProductsAdapter;
import com.tech.arinzedroid.starchoiceadmin.interfaces.RemoveProductInterface;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.model.UserProductsModel;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectProductFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        RemoveProductInterface {


    @BindView(R.id.spinner)
    Spinner productItemsSpinner;
    @BindView(R.id.recycler_view)
    RecyclerView productRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;

    private List<UserProductsModel> selectedProducts = new ArrayList<>();
    private List<ProductsModel> adapterList = new ArrayList<>();
    private ProductsAdapter productAdapter;
    private ClientsModel userModel;

    private static final String USER_MODEL= "USER_MODEL";

    private OnRegisterInterface onRegisterInterface;
    private AppViewModel appViewModel;

    public SelectProductFragment() {
        // Required empty public constructor
    }

    public static SelectProductFragment newInstance(ClientsModel userModel){
        SelectProductFragment fragment = new SelectProductFragment();
        Bundle arg = new Bundle();
        arg.putParcelable(USER_MODEL,Parcels.wrap(userModel));
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            userModel = Parcels.unwrap(getArguments().getParcelable(USER_MODEL));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_product, container, false);
        ButterKnife.bind(this,view);

        if(appViewModel != null){
            getAndDisplayProducts();
        }else{
            appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);
            getAndDisplayProducts();
        }

        return view;
    }

    private void getAndDisplayProducts() {
        appViewModel.getAllProducts(false).observe(this, productsModels -> {
            if(productsModels != null){
                ArrayAdapter<ProductsModel> spinnerAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item,productsModels);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                productAdapter = new ProductsAdapter(adapterList,this);
                productRecyclerView.setAdapter(productAdapter);

                productItemsSpinner.setAdapter(spinnerAdapter);
                productItemsSpinner.setOnItemSelectedListener(this);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);
    }

    private void displaySelectedItem(ProductsModel productModel){
        UserProductsModel userProductsModel = new UserProductsModel();
        userProductsModel.setProductModel(productModel);
        userProductsModel.setProductId(productModel.getId());
        userProductsModel.setUserId(userModel.getId());
        userProductsModel.setUserModel(userModel);
        userProductsModel.setDateCreated(new Date());
        selectedProducts.add(userProductsModel);
        productAdapter.addProduct(productModel);
    }

    @OnClick(R.id.confirm_btn)
    public void onConfirm(View v){
        progressBar.setVisibility(View.VISIBLE);
        confirmBtn.setEnabled(false);
        appViewModel.addUserProducts(selectedProducts).observe(this, isSuccessful -> {
            if(isSuccessful != null && isSuccessful){
                Toast.makeText(getActivity(), "Products added to clients acct successfully", Toast.LENGTH_SHORT).show();
                if(getActivity() != null){
                    getActivity().finish();
                }
            }else{
                confirmBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Product could not be added to this client. " +
                        "Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
        //onRegisterInterface.onRegister(selectedProducts);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterInterface) {
            onRegisterInterface = (OnRegisterInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRegisterInterface = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        displaySelectedItem((ProductsModel) adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void removeProduct(int position) {
        if(!selectedProducts.isEmpty() && selectedProducts.get(position) != null){
            selectedProducts.remove(position);
            productAdapter.removeProduct(position);
        }
    }

    public interface OnRegisterInterface {
        // TODO: Update argument type and name
        void onRegister(List<UserProductsModel> userProductModels);
    }
}
