package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.interfaces.RemoveProductInterface;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.FormatUtil;
import com.tech.arinzedroid.starchoiceadmin.viewHolder.ProductsViewHolder;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {

    private List<ProductsModel> productModelList;
    private RemoveProductInterface removeProductInterface;

    public ProductsAdapter(List<ProductsModel> productModels, RemoveProductInterface removeProductInterface){
        this.productModelList = productModels;
        this.removeProductInterface = removeProductInterface;
        setHasStableIds(true);
    }

    public void addProduct(ProductsModel productModel){
        this.productModelList.add(productModel);
        notifyItemInserted(this.productModelList.size() - 1);
    }

    public void removeProduct(int position){
        productModelList.remove(position);
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position,productModelList.size());
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items,parent,false);
        return new ProductsViewHolder(v,removeProductInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        ProductsModel data = productModelList.get(position);
        holder.nameTv.setText(data.getProductName());
        holder.priceTv.setText(FormatUtil.formatPrice(data.getPrice()));
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }
}
