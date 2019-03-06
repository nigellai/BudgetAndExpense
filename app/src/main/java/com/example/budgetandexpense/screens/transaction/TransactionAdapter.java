package com.example.budgetandexpense.screens.transaction;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetandexpense.R;
import com.example.budgetandexpense.components.click_listeners.OnDeleteListener;
import com.example.budgetandexpense.components.click_listeners.OnEditListener;
import com.example.budgetandexpense.model.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Transaction> transactionList;
    private OnDeleteListener deleteListener;
    private OnEditListener editListener;

    public TransactionAdapter(List<Transaction> transactionList, OnDeleteListener deleteListener, OnEditListener editListener) {
        this.transactionList = transactionList;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction, parent,
                false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        TransactionViewHolder transactionViewHolder =(TransactionViewHolder) holder;
        transactionViewHolder.bindData(transactionList.get(position));
        transactionViewHolder.getView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteListener.deleteItem(transactionList.get(position));
                return true;
            }
        });
        transactionViewHolder.getView().setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                deleteListener.deleteItem(transactionList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
