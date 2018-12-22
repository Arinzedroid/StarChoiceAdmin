package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.interfaces.ClientItemClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.interfaces.DeleteClientInterface;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.DateTimeUtils;
import com.tech.arinzedroid.starchoiceadmin.utils.FormatUtil;
import com.tech.arinzedroid.starchoiceadmin.viewHolder.ClientViewHolder;

import java.util.Date;
import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientViewHolder> {

    private List<ClientsModel> clientsModelList;
    private DeleteClientInterface deleteClientInterface;
    private ClientItemClickedInterface clientItemClickedInterface;
    private int total = 0; private double totalAmt = 0; private Date date; private int count;

    public ClientsAdapter(List<ClientsModel> clientsModelsList, DeleteClientInterface deleteClientInterface,
                          ClientItemClickedInterface clientItemClickedInterface){
        this.clientsModelList = clientsModelsList; count = clientsModelsList.size();
        this.deleteClientInterface = deleteClientInterface;
        this.clientItemClickedInterface = clientItemClickedInterface;
    }

    public void addAll(List<ClientsModel> clientsModels){
        clientsModelList.clear();
        clientsModelList.addAll(clientsModels);
        count = clientsModels.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_clients,parent,false);
        return new ClientViewHolder(view,deleteClientInterface,clientItemClickedInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        ClientsModel data = clientsModelList.get(position);
        holder.nameTv.setText(data.getFullname());
        holder.clientDateCreatedTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
        holder.serialTv.setText(String.valueOf(count - position));

        calculateDataToDisplay(position,holder);
        //computeAndDisplayViews(holder,position);
    }

    private void computeAndDisplayViews(ClientViewHolder holder, int position){
        ClientsModel data = clientsModelList.get(position);
        holder.nameTv.setText(data.getFullname());
        holder.clientDateCreatedTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
        holder.serialTv.setText(String.valueOf(count - position));

        total++;
        totalAmt += data.getTotalAmount();

        if(position == 0){
            date = data.getDateCreated();
            holder.dateTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
            holder.itemsLayout1.setVisibility(View.VISIBLE);
            displayTotalsView(holder,position,data);
        }else{
            if(DateTimeUtils.isSameDay(data.getDateCreated(),date)){
                holder.itemsLayout1.setVisibility(View.GONE);
                displayTotalsView(holder, position, data);

            }else{
                date = data.getDateCreated();
                holder.dateTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
                holder.itemsLayout1.setVisibility(View.VISIBLE);
                displayTotalsView(holder,position,data);
            }
        }
    }

    private void displayTotalsView(ClientViewHolder holder, int position, ClientsModel data) {
        if(clientsModelList.size() > position + 1){
            ClientsModel data2 = clientsModelList.get(position + 1);
            try{
                if(DateTimeUtils.isDateBefore(data2.getDateCreated(),data.getDateCreated())){
                    holder.totalAmtTv.setText(FormatUtil.formatPrice(totalAmt));
                    holder.totalClientsTv.setText(String.valueOf(total));
                    holder.itemsLayout3.setVisibility(View.VISIBLE);
                    total = 0;
                    totalAmt = 0;
                }
            }catch (Exception e){
                Log.e("Adapter","Error >>> ",e);
            }
        }else{
            holder.totalAmtTv.setText(FormatUtil.formatPrice(totalAmt));
            holder.totalClientsTv.setText(String.valueOf(total));
            holder.itemsLayout3.setVisibility(View.VISIBLE);
        }
    }

    private void calculateDataToDisplay(int position, ClientViewHolder holder){
        Date first_date  = clientsModelList.get(position).getDateCreated();

        //check that the last visible item is not the last item in the list.
        //if yes calculate and display totals
        if(position + 1 < clientsModelList.size()){
            Date second_date = clientsModelList.get(position + 1).getDateCreated();
            if(DateTimeUtils.isSameDay(second_date,first_date)){
                holder.itemsLayout3.setVisibility(View.GONE);
                //check this is not the first item so as to hide the date textview
                if(position != 0){
                    holder.itemsLayout1.setVisibility(View.GONE);
                }
            }else {
                try {
                    if(DateTimeUtils.isDateBefore(second_date,first_date)){
                        holder.itemsLayout3.setVisibility(View.VISIBLE);
                        computeAndDisplayTotals(first_date,holder);
                        //date = second_date;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            computeAndDisplayTotals(first_date,holder);
        }
    }

    private void computeAndDisplayTotals(Date date, ClientViewHolder holder){
        int totalNo = 0; double totalAmt = 0;
        for (ClientsModel clientsModel : clientsModelList) {
            if(DateTimeUtils.isSameDay(clientsModel.getDateCreated(),date)){
                totalNo++;
                totalAmt += clientsModel.getTotalAmount();
            }
        }
        holder.totalClientsTv.setText(String.valueOf(totalNo));
        holder.totalAmtTv.setText(FormatUtil.formatPrice(totalAmt));
    }

    @Override
    public int getItemCount() {
        return clientsModelList.size();
    }
}
