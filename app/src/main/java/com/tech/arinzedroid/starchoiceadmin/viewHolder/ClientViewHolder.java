package com.tech.arinzedroid.starchoiceadmin.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.interfaces.ClientItemClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.interfaces.DeleteClientInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.item_layout1)
    public View itemsLayout1;
    @BindView(R.id.item_layout2)
    public View itemsLayout2;
    @BindView(R.id.item_layout3)
    public View itemsLayout3;
    @BindView(R.id.date)
    public TextView dateTv;
    @BindView(R.id.total_clients)
    public TextView totalClientsTv;
    @BindView(R.id.total_amt)
    public TextView totalAmtTv;
    @BindView(R.id.name)
    public TextView nameTv;
    @BindView(R.id.serial)
    public TextView serialTv;
    @BindView(R.id.client_date_created)
    public TextView clientDateCreatedTv;
    @BindView(R.id.delete)
    ImageButton deleteBtn;
    @BindView(R.id.item_layout)
    View mockLayout;

    private DeleteClientInterface deleteClientInterface;
    private ClientItemClickedInterface clientItemClickedInterface;

    public ClientViewHolder(View itemView, DeleteClientInterface deleteClientInterface,
                            ClientItemClickedInterface clientItemClickedInterface) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.deleteClientInterface = deleteClientInterface;
        this.clientItemClickedInterface = clientItemClickedInterface;
        deleteBtn.setOnClickListener(this);
        mockLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:{
                deleteClientInterface.onDelete(this.getLayoutPosition());
                break;
            }
            case R.id.item_layout:{
                clientItemClickedInterface.client(this.getLayoutPosition());
                break;
            }
        }
    }
}
