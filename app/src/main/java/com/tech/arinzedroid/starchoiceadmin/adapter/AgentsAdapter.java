package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.interfaces.AgentItemClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.DateTimeUtils;
import com.tech.arinzedroid.starchoiceadmin.viewHolder.AgentsViewHolder;

import java.util.List;

public class AgentsAdapter extends RecyclerView.Adapter<AgentsViewHolder> {

    private List<AgentsModel> agentsModelsList;
    private AgentItemClickedInterface agentItemClickedInterface; private int count;

    public AgentsAdapter(List<AgentsModel> agentsModelsList,
                         AgentItemClickedInterface agentItemClickedInterface){
        this.agentsModelsList = agentsModelsList; count = agentsModelsList.size();
        this.agentItemClickedInterface = agentItemClickedInterface;
    }

    public void addAll(List<AgentsModel> data){
        agentsModelsList.clear();
        agentsModelsList.addAll(data);
        count = agentsModelsList.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AgentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_agents,parent,false);
        return new AgentsViewHolder(view,agentItemClickedInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentsViewHolder holder, int position) {
        AgentsModel data = agentsModelsList.get(position);
        holder.nameTv.setText(String.valueOf(data.getFirstname() + " " + data.getLastname()));
        holder.statusTv.setText(String.valueOf("active"));
        holder.dateTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
        holder.serialTv.setText(String.valueOf(count - position));
    }

    @Override
    public int getItemCount() {
        return agentsModelsList.size();
    }
}
