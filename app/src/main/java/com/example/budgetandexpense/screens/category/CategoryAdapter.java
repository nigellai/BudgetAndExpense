package com.example.budgetandexpense.screens.category;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetandexpense.R;
import com.example.budgetandexpense.model.Category;

import java.util.List;

import com.example.budgetandexpense.components.click_listeners.OnDeleteListener;
import com.example.budgetandexpense.components.click_listeners.OnEditListener;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> categoryList;
    private OnEditListener editListener;

    public CategoryAdapter(List<Category> categoryList, OnEditListener
            editListener) {
        this.categoryList = categoryList;
        this.editListener = editListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent,
                false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        CategoryViewHolder categoryViewHolder =(CategoryViewHolder)holder;
        categoryViewHolder.binData(categoryList.get(position));
        categoryViewHolder.getView().setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                editListener.editItem(categoryList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
