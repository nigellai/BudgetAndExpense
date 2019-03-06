package com.example.budgetandexpense.screens.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetandexpense.R;
import com.example.budgetandexpense.model.Category;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private PieChart pieChart;
    private double sumBudget = 0;

    private PieDataSet injectCategories() {
        List<Category> categories = Category.getAllCategories();

        if (categories.size() == 0) {
            return null;
        }

        for(Category category : categories) {
            sumBudget += category.budget;
        }

        List<PieEntry> strings = new ArrayList<>();
        for(Category category : categories) {
            strings.add(new PieEntry((float)(category.budget/sumBudget)*100, category.name));
        }
        PieDataSet dataSet = new PieDataSet(strings,"Categories");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(Category category : categories) {
            colors.add(category.colour);
        }

        dataSet.setColors(colors);

        return dataSet;
    }

    private void initChart() {
        PieDataSet dataSet = injectCategories();
        if( dataSet!=null ) {
            PieData pieData = new PieData(dataSet);
            pieData.setDrawValues(true);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(12f);
            pieData.setValueTextColor(Color.WHITE);

            pieChart.setData(pieData);
            pieChart.invalidate();

            Description description = new Description();
            description.setText("");
            pieChart.setDescription(description);
            pieChart.setHoleRadius(0f);
            pieChart.setTransparentCircleRadius(0f);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_layout, container, false);
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        initChart();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initChart();
    }
}
