package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.interfaces.ProductItemClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.DateTimeUtils;
import com.tech.arinzedroid.starchoiceadmin.utils.FormatUtil;
import com.tech.arinzedroid.starchoiceadmin.viewHolder.ProductsViewHolder;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {

    private List<ProductsModel> productsModelList;
    private ProductItemClickedInterface productItemClickedInterface; private int count;

    public ProductsAdapter(List<ProductsModel> productsModels,
                           ProductItemClickedInterface productItemClickedInterface){
        this.productsModelList = productsModels; count = productsModelList.size();
        this.productItemClickedInterface = productItemClickedInterface;
    }

    public void addAll(List<ProductsModel> data){
        productsModelList.clear();
        productsModelList.addAll(data); count = productsModelList.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_products,parent,false);
        return new ProductsViewHolder(v,productItemClickedInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        ProductsModel data = productsModelList.get(position);
        holder.dateTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
        holder.nameTv.setText(data.getProductName());
        holder.serialTv.setText(String.valueOf(count - position));

        if(data.isActive())
            holder.statusTv.setText(String.valueOf("active"));
        else holder.statusTv.setText(String.valueOf("inactive"));

        holder.priceTv.setText(FormatUtil.formatPrice(data.getPrice()));
    }

    @Override
    public int getItemCount() {
        return productsModelList.size();
    }
}
