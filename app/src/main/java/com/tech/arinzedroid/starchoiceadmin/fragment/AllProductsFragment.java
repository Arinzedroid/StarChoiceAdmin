package com.tech.arinzedroid.starchoiceadmin.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.activity.RegisterProductActivity;
import com.tech.arinzedroid.starchoiceadmin.adapter.AllProductsAdapter;
import com.tech.arinzedroid.starchoiceadmin.interfaces.ProductItemClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllProductsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        ProductItemClickedInterface{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.admin_name)
    TextView adminNameTv;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;


    private String adminName = "";
    private AllProductsAdapter allProductsAdapter;
    private AppViewModel appViewModel;
    private List<ProductsModel> productsModel;


    public AllProductsFragment() {
        // Required empty public constructor
    }


    public static AllProductsFragment newInstance(String adminName) {
        AllProductsFragment fragment = new AllProductsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ADMIN_NAME,adminName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           adminName = getArguments().getString(Constants.ADMIN_NAME);
        }
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this,v);

        adminNameTv.setText(adminName);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        loadData(false);

        return v;
    }

    private void loadData(boolean refresh){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAllProducts(refresh).observe(this, productsModels -> {
           swipeRefreshLayout.setRefreshing(false);
           if(productsModels != null){
               noDataTv.setVisibility(View.GONE);
               this.productsModel = productsModels;
               if(allProductsAdapter != null){
                   allProductsAdapter.addAll(productsModels);
               }else{
                   allProductsAdapter = new AllProductsAdapter(productsModels, this);
                   recyclerView.setAdapter(allProductsAdapter);
               }
           }else noDataTv.setVisibility(View.VISIBLE);
        });
    }

    @OnClick(R.id.fab)
    public void onFabClick(View v){
        startActivity(new Intent(getActivity(), RegisterProductActivity.class));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onClick(int position) {
        if(productsModel != null && !productsModel.isEmpty() && productsModel.size() > position){
            Intent intent = new Intent(getActivity(),RegisterProductActivity.class);
            intent.putExtra(Constants.PRODUCT_DATA, Parcels.wrap(productsModel.get(position)));
            startActivity(intent);
        }
    }
}
