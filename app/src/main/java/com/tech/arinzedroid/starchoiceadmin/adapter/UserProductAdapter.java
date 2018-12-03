package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.interfaces.UserProductClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.model.UserProductsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.DateTimeUtils;
import com.tech.arinzedroid.starchoiceadmin.utils.FormatUtil;
import com.tech.arinzedroid.starchoiceadmin.viewHolder.UserProductViewHolder;

import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductViewHolder> {

    private List<UserProductsModel> userProductsModels;
    private UserProductClickedInterface userProductClickedInterface;

    public UserProductAdapter(List<UserProductsModel> userProductsModels,
                              UserProductClickedInterface userProductClickedInterface){
        this.userProductsModels = userProductsModels;
        this.userProductClickedInterface = userProductClickedInterface;
    }

    public void addProducts(List<UserProductsModel> userProductsModels){
        int count = this.userProductsModels.size();
        this.userProductsModels.addAll(count,userProductsModels);
        notifyItemRangeInserted(count,this.userProductsModels.size());
    }

    public void addProduct(int position, UserProductsModel userProductsModel){
        this.userProductsModels.add(position,userProductsModel);
        notifyItemInserted(position);
    }

    public void clearData(){
        this.userProductsModels.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        this.userProductsModels.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public UserProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_product_items,parent,false);
        return new UserProductViewHolder(v,userProductClickedInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProductViewHolder holder, int position) {
        UserProductsModel userProductsModel = this.userProductsModels.get(position);
        holder.dateTv.setText(DateTimeUtils.parseDateTime(userProductsModel.getDateCreated()));
        holder.amtPaidTv.setText(FormatUtil.formatPrice(userProductsModel.getAmtPaid()));
        ProductsModel data = userProductsModel.getProductModel();
        if(data != null){
            holder.productNameTv.setText(data.getProductName());
            holder.productPriceTv.setText(FormatUtil.formatPrice(data.getPrice()));
            double bal = data.getPrice() - userProductsModel.getAmtPaid();
            holder.amtRemainingTv.setText(FormatUtil.formatPrice(bal));
        }

    }

    @Override
    public int getItemCount() {
        return userProductsModels.size();
    }
}
