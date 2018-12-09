package com.tech.arinzedroid.starchoiceadmin.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.activity.RegisterAgentActivity;
import com.tech.arinzedroid.starchoiceadmin.activity.ViewAgentActivity;
import com.tech.arinzedroid.starchoiceadmin.adapter.AgentsAdapter;
import com.tech.arinzedroid.starchoiceadmin.interfaces.AgentItemClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;
import com.tech.arinzedroid.starchoiceadmin.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AgentItemClickedInterface{


    public AgentsFragment() {
        // Required empty public constructor
    }

    private AppViewModel appViewModel;
    private AgentsAdapter agentsAdapter;
    private String adminName = "";
    private List<AgentsModel> agentsModelList;
    private List<AgentsModel> searchedAgentModelList;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.admin_name)
    TextView adminNameTv;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;

    public static AgentsFragment newInstance(String adminName) {
        AgentsFragment fragment = new AgentsFragment();
        Bundle args = new Bundle();
        args.putString("name",adminName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);
        if(getArguments() != null){
            adminName = getArguments().getString("name");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_agents, container, false);
        ButterKnife.bind(this,v);

        adminNameTv.setText(adminName);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        loadData(false);

        return v;
    }

    private void onSearchQuery(){
        appViewModel.getQuery().observe(this, query -> {
            if(this.getUserVisibleHint()){
                if(!TextUtils.isEmpty(query)){
                    if(agentsModelList != null && !agentsModelList.isEmpty()){
                        List<AgentsModel> agentsModels = new ArrayList<>();
                        for(AgentsModel agents : agentsModelList){
                            if(agents.getLastname().toLowerCase().contains(query.toLowerCase()) ||
                                    agents.getFirstname().toLowerCase().contains(query.toLowerCase())){
                                agentsModels.add(agents);
                            }
                        }
                        searchedAgentModelList = agentsModels;
                        agentsAdapter = new AgentsAdapter(agentsModels,this);
                        recyclerView.setAdapter(agentsAdapter);
                    }
                }else{
                    searchedAgentModelList = agentsModelList;
                   agentsAdapter.addAll(agentsModelList);
                }
            }
        });
    }

    private void loadData(boolean refresh){
        swipeRefreshLayout.setRefreshing(true);
        appViewModel.getAllAgents(refresh).observe(this, agents -> {
            swipeRefreshLayout.setRefreshing(false);
            if(agents != null){
                agentsModelList = agents;
                searchedAgentModelList = agents;
                noDataTv.setVisibility(View.GONE);
                if(agentsAdapter == null){
                    agentsAdapter = new AgentsAdapter(agents,this);
                    recyclerView.setAdapter(agentsAdapter);
                }else{
                    agentsAdapter.addAll(agents);
                }
                onSearchQuery();
            }else{
                noDataTv.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.fab)
    public void onFabClick(View v){
        startActivity(new Intent(getActivity(),RegisterAgentActivity.class));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onClick(int position) {
        if(searchedAgentModelList != null && !searchedAgentModelList.isEmpty() && searchedAgentModelList.size() > position){
            Intent intent = new Intent(getActivity(), ViewAgentActivity.class);
            intent.putExtra(Constants.AGENT_DATA, Parcels.wrap(searchedAgentModelList.get(position)));
            startActivity(intent);
        }else
            Toast.makeText(getContext(), "Invalid agent selected", Toast.LENGTH_SHORT).show();
    }
}
