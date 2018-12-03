package com.tech.arinzedroid.starchoiceadmin.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.interfaces.ProductItemClickedInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.name)
    public TextView nameTv;
    @BindView(R.id.price)
    public TextView priceTv;
    @BindView(R.id.date)
    public TextView dateTv;
    @BindView(R.id.status)
    public TextView statusTv;
    @BindView(R.id.serial)
    public TextView serialTv;
    @BindView(R.id.item_layout)
    View layout;

    private ProductItemClickedInterface productItemClickedInterface;

    public AllProductsViewHolder(View itemView, ProductItemClickedInterface productItemClickedInterface) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        layout.setOnClickListener(this);
        this.productItemClickedInterface = productItemClickedInterface;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.item_layout)
            productItemClickedInterface.onClick(this.getLayoutPosition());
    }
}
