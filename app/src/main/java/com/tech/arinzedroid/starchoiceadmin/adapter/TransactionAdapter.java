package com.tech.arinzedroid.starchoiceadmin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
    private Date date = new Date(); private TransactionItemClickedInterface transactionItemClickedInterface;
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
        TransactionsModel data = transactionsModelsList.get(position);
        holder.amtTv.setText(FormatUtil.formatPrice(data.getAmount()));
        holder.serialTv.setText(String.valueOf(count - position));
        holder.statusTv.setText(data.getStatus());
        holder.dateTv.setText(DateTimeUtils.parseDateTime(data.getDateCreated()));

        calculateDataToDisplay(position,holder);
    }

    private void analyseDataAtPosition(TransactionViewHolder holder, int position){
        TransactionsModel data = transactionsModelsList.get(position);
        holder.amtTv.setText(FormatUtil.formatPrice(data.getAmount()));
        holder.serialTv.setText(String.valueOf(count - position));
        holder.statusTv.setText(data.getStatus());

//        total++;
//        totalAmt += data.getAmount();

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

    private void generateTotal(Date date){
        total = 0; totalAmt = 0;
        for(TransactionsModel trans : transactionsModelsList){
            if(DateTimeUtils.isSameDay(trans.getDateCreated(),date)){
                totalAmt = trans.getAmount();
                total++;
            }
        }
    }


    private void displayTotal(TransactionViewHolder holder, int position, TransactionsModel data) {
        if(transactionsModelsList.size() > position + 1){
            TransactionsModel data2 = transactionsModelsList.get(position + 1);
            try{
                if(DateTimeUtils.isDateBefore(data2.getDateCreated(),data.getDateCreated())){
                    generateTotal(data.getDateCreated());
                    holder.totalAmtTv.setText(FormatUtil.formatPrice(totalAmt));
                    holder.totalSalesTv.setText(String.valueOf(total));
                    holder.itemsLayout3.setVisibility(View.VISIBLE);
//                    total = 0;
//                    totalAmt = 0;
                }
            }catch (Exception e){
                Log.e("Adapter","Error >>> ",e);
            }
        }else{
            //generateTotal(data.getDateCreated());
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

    private void calculateDataToDisplay(int position, TransactionViewHolder holder){
        Date first_date  = transactionsModelsList.get(position).getDateCreated();

        //check that the last visible item is not the last item in the list.
        //if yes calculate and display totals
        if(position + 1 < transactionsModelsList.size()){
            Date second_date = transactionsModelsList.get(position + 1).getDateCreated();
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

    private void computeAndDisplayTotals(Date date, TransactionViewHolder holder){
        int totalNo = 0; double totalAmt = 0;
        for (TransactionsModel transModel : transactionsModelsList) {
            if(DateTimeUtils.isSameDay(transModel.getDateCreated(),date)){
                totalNo++;
                totalAmt += transModel.getAmount();
            }
        }
        holder.totalSalesTv.setText(String.valueOf(totalNo));
        holder.totalAmtTv.setText(FormatUtil.formatPrice(totalAmt));
    }

    @Override
    public int getItemCount() {
        return transactionsModelsList.size();
    }
}
