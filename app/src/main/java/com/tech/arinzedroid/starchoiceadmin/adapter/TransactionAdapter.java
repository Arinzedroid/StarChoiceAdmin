package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.interfaces.TransactionItemClickedInterface;
import com.tech.arinzedroid.starchoiceadmin.model.TransactionsModel;
import com.tech.arinzedroid.starchoiceadmin.utils.DateTimeUtils;
import com.tech.arinzedroid.starchoiceadmin.utils.FormatUtil;
import com.tech.arinzedroid.starchoiceadmin.viewHolder.TransactionViewHolder;

import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

    private List<TransactionsModel> transactionsModelsList;
    private Date date; private TransactionItemClickedInterface transactionItemClickedInterface;
    private double totalAmt = 0; private int total = 0,count;

    public TransactionAdapter(List<TransactionsModel> transactionsModelsList,
                              TransactionItemClickedInterface transactionItemClickedInterface){
        this.transactionsModelsList = transactionsModelsList;
        count = transactionsModelsList.size();
        this.transactionItemClickedInterface = transactionItemClickedInterface;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_transactions,parent,false);
        return new TransactionViewHolder(v,transactionItemClickedInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
      compute(holder,position);
    }

    private void compute(TransactionViewHolder holder, int position){
        TransactionsModel data = transactionsModelsList.get(position);
        holder.amtTv.setText(FormatUtil.formatPrice(data.getAmount()));
        holder.serialTv.setText(String.valueOf(count - position));
        holder.statusTv.setText(data.getStatus());

        total++;
        totalAmt += data.getAmount();

        if(position == 0){
            date = data.getDateCreated();
            holder.dateTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
            holder.itemsLayout1.setVisibility(View.VISIBLE);
            displayTotal(holder,position,data);
        }else{
            if(DateTimeUtils.isSameDay(data.getDateCreated(),date)){
                holder.itemsLayout1.setVisibility(View.GONE);
                displayTotal(holder, position, data);

            }else{
                date = data.getDateCreated();
                holder.dateTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));
                holder.itemsLayout1.setVisibility(View.VISIBLE);
                displayTotal(holder,position,data);
            }
        }
    }

    private void displayTotal(TransactionViewHolder holder, int position, TransactionsModel data) {
        if(transactionsModelsList.size() > position + 1){
            TransactionsModel data2 = transactionsModelsList.get(position + 1);
            try{
                if(DateTimeUtils.isDateBefore(data2.getDateCreated(),data.getDateCreated())){
                    holder.totalAmtTv.setText(FormatUtil.formatPrice(totalAmt));
                    holder.totalSalesTv.setText(String.valueOf(total));
                    holder.itemsLayout3.setVisibility(View.VISIBLE);
                    total = 0;
                    totalAmt = 0;
                }
            }catch (Exception e){
                Log.e("Adapter","Error >>> ",e);
            }
        }else{
            holder.totalAmtTv.setText(FormatUtil.formatPrice(totalAmt));
            holder.totalSalesTv.setText(String.valueOf(total));
            holder.itemsLayout3.setVisibility(View.VISIBLE);
        }
    }

    public void updateTransaction(int position, TransactionsModel transactionsModel){
       this.transactionsModelsList.set(position,transactionsModel);
       notifyItemChanged(position);
    }

    public void deleteTransaction(int position,TransactionsModel transactionsModel){
        this.transactionsModelsList.remove(transactionsModel);
        notifyItemRemoved(position);
    }

    public void addItem(int position, TransactionsModel transactionsModel){
        this.transactionsModelsList.add(position,transactionsModel);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return transactionsModelsList.size();
    }
}
