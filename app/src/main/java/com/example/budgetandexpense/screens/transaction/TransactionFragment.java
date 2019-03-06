package com.example.budgetandexpense.screens.transaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetandexpense.R;
import com.example.budgetandexpense.components.click_listeners.OnDeleteListener;
import com.example.budgetandexpense.components.click_listeners.OnEditListener;
import com.example.budgetandexpense.model.Transaction;
import com.example.budgetandexpense.screens.EditCategoryActivity;
import com.example.budgetandexpense.screens.EditTransactionActivity;

import java.util.List;

public class TransactionFragment extends Fragment implements OnEditListener, OnDeleteListener {

    private FloatingActionButton addTransactionButton;
    private RecyclerView transactionListRecycleView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_layout, container, false);
        addTransactionButton = (FloatingActionButton) view.findViewById(R.id.addTransactionButton);
        transactionListRecycleView = (RecyclerView) view.findViewById(R.id.transactionListRecyclerView);

        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditTransactionActivity.class);
                startActivity(intent);
            }
        });
        transactionList = Transaction.getAllTransactions();
        transactionAdapter = new TransactionAdapter(transactionList, this, this);
        transactionListRecycleView.setAdapter(transactionAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        transactionListRecycleView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(transactionListRecycleView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        transactionListRecycleView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        transactionList = Transaction.getAllTransactions();
        transactionAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteItem(final Object object) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setMessage(R.string.dialog_delete_transaction)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Transaction.deleteTransaction((Transaction)object);
                        transactionList = Transaction.getAllTransactions();
                        transactionAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel_button, null)
                .create();
        dialog.show();
    }

    @Override
    public void editItem(Object object, int index) {

    }
}
