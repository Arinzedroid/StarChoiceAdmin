package com.tech.arinzedroid.starchoiceadmin.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tech.arinzedroid.starchoiceadmin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_layout1)
    public View itemsLayout1;
    @BindView(R.id.item_layout2)
    public View itemsLayout2;
    @BindView(R.id.item_layout3)
    public View itemsLayout3;
    @BindView(R.id.date)
    public TextView dateTv;
    @BindView(R.id.amt)
    public TextView amtTv;
    @BindView(R.id.total_sales)
    public TextView totalSalesTv;
    @BindView(R.id.total_amt)
    public TextView totalAmtTv;
    @BindView(R.id.status)
    public TextView statusTv;
    @BindView(R.id.serial)
    public TextView serialTv;

    public TransactionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
