package com.tech.arinzedroid.starchoiceadmin.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.interfaces.AgentItemClickedInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.item_layout)
    View layoutItem;
    @BindView(R.id.name)
    public TextView nameTv;
    @BindView(R.id.status)
    public TextView statusTv;
    @BindView(R.id.date)
    public TextView dateTv;
    @BindView(R.id.serial)
    public TextView serialTv;

    private AgentItemClickedInterface agentItemClickedInterface;

    public AgentsViewHolder(View itemView, AgentItemClickedInterface agentItemClickedInterface) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.agentItemClickedInterface = agentItemClickedInterface;
        layoutItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.item_layout)
            agentItemClickedInterface.onClick(this.getLayoutPosition());
    }
}
