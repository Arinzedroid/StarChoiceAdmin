package com.tech.arinzedroid.starchoiceadmin.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tech.arinzedroid.starchoiceadmin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedPaymentsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.product_name)
    public TextView productName;
    @BindView(R.id.client_name)
    public TextView clientName;
    @BindView(R.id.agent_name)
    public TextView agentName;
    @BindView(R.id.amount_paid)
    public TextView amountPaid;
    @BindView(R.id.product_price)
    public TextView productPrice;
    @BindView(R.id.date)
    public TextView date;
    @BindView(R.id.sn)
    public TextView serialNo;


    public CompletedPaymentsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
