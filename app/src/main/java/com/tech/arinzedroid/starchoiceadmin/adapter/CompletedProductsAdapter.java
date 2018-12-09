package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.model.CompletedProducts;
import com.tech.arinzedroid.starchoiceadmin.utils.DateTimeUtils;
import com.tech.arinzedroid.starchoiceadmin.utils.FormatUtil;
import com.tech.arinzedroid.starchoiceadmin.viewHolder.CompletedPaymentsViewHolder;

import java.util.List;

public class CompletedProductsAdapter extends RecyclerView.Adapter<CompletedPaymentsViewHolder> {
    private List<CompletedProducts> completedProductsList; private int count;

    public CompletedProductsAdapter(List<CompletedProducts> completedProductsList){
        this.completedProductsList = completedProductsList;
        count = completedProductsList.size();
    }

    public void addAll(List<CompletedProducts> completedProductsList){
        this.completedProductsList.clear();
        this.completedProductsList = completedProductsList;
        count = completedProductsList.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CompletedPaymentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_completed_payments,parent,false);
        return new CompletedPaymentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedPaymentsViewHolder holder, int position) {
        CompletedProducts products = completedProductsList.get(position);
        holder.serialNo.setText(String.valueOf(count - position));

        if(products.getProductModel() != null){
            holder.productName.setText(products.getProductModel().getProductName());
            holder.productPrice.setText(FormatUtil.formatPrice(products.getProductModel().getPrice()));
            holder.date.setText(DateTimeUtils.parseDateTime(products.getDateUpdated()));
            holder.amountPaid.setText(FormatUtil.formatPrice(products.getAmtPaid()));
        }

        if(products.getUserModel() != null && !TextUtils.isEmpty(products.getUserModel().getAgentName())
                && !TextUtils.isEmpty(products.getUserModel().getFullname())){
            holder.clientName.setText(String.valueOf("Client: " + products.getUserModel().getFullname()));
            holder.agentName.setText(String.valueOf("Agent: " + products.getUserModel().getAgentName()));
        }
    }

    @Override
    public int getItemCount() {
        return completedProductsList.size();
    }
}
