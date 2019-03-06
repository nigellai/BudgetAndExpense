package com.example.budgetandexpense.screens.transaction;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.budgetandexpense.R;
import com.example.budgetandexpense.model.Transaction;

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewTransactionCategoryName;
    private TextView textViewTransactionValue;
    private TextView textViewTransactionTime;

    public TransactionViewHolder(View view) {
        super(view);
        this.view = view;
        textViewTransactionCategoryName = view.findViewById(R.id.textViewTransactionCategoryName);
        textViewTransactionValue = view.findViewById(R.id.textViewTransactionValue);
        textViewTransactionTime = view.findViewById(R.id.textViewTransactionTime);
    }

    public void bindData(Transaction transaction) {
        textViewTransactionCategoryName.setText(transaction.categoryName);
        textViewTransactionValue.setText(String.valueOf(transaction.value));
        textViewTransactionTime.setText(String.valueOf(transaction.transactionDate));
    }

    public View getView() {
        return view;
    }
}
