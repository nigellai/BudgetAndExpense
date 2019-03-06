package com.example.budgetandexpense.screens.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetandexpense.components.click_listeners.OnEditListener;
import com.example.budgetandexpense.model.Category;
import com.example.budgetandexpense.screens.EditCategoryActivity;
import com.example.budgetandexpense.R;

import java.util.List;

public class CateogryFragment extends Fragment implements OnEditListener {

    private FloatingActionButton addCategoryButton;
    private RecyclerView categoryListRecycleView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_layout, container, false);
        addCategoryButton = (FloatingActionButton) view.findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditCategoryActivity.class);
                startActivity(intent);
            }
        });


        categoryListRecycleView = (RecyclerView) view.findViewById(R.id.categoryListRecyclerView);
        categoryList = Category.getAllCategories();
        categoryAdapter = new CategoryAdapter(categoryList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        categoryListRecycleView.setAdapter(categoryAdapter);
        categoryListRecycleView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(categoryListRecycleView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        categoryListRecycleView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        categoryList = Category.getAllCategories();
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void editItem(Object object, int index) {
        Category selectedCategory = categoryList.get(index);
        Intent intent = new Intent(getActivity(), EditCategoryActivity.class);
        intent.putExtra("name", selectedCategory.name);
        intent.putExtra("budget", selectedCategory.budget);
        intent.putExtra("color", selectedCategory.colour);
        startActivity(intent);
    }

}
