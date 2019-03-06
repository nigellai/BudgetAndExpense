package com.example.budgetandexpense.screens.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.budgetandexpense.R;
import com.example.budgetandexpense.model.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewCategoryName;
    private TextView textViewCategoryBudget;
    private TextView textViewExceedIndicator;

    public CategoryViewHolder(View view) {
        super(view);
        this.view = view;
        textViewCategoryName = (TextView) view.findViewById(R.id.textViewCategoryName);
        textViewCategoryBudget = (TextView) view.findViewById(R.id.textViewCategoryBudget);
        textViewExceedIndicator = (TextView) view.findViewById(R.id.textViewExceedIndicator);
    }

    public void binData(Category category) {
        textViewCategoryName.setText(category.name);
        textViewCategoryBudget.setText(String.valueOf(category.budget));
        textViewExceedIndicator.setVisibility(Category.ifExceeded(category.name) ? View.VISIBLE : View.INVISIBLE);
        view.setBackgroundColor(category.colour);
    }

    public View getView() {
        return view;
    }
}
