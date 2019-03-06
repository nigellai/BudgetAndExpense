package com.example.budgetandexpense.screens;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.budgetandexpense.R;
import com.example.budgetandexpense.screens.category.CateogryFragment;
import com.example.budgetandexpense.screens.dashboard.DashboardFragment;
import com.example.budgetandexpense.screens.transaction.TransactionFragment;

public class MainActivity extends AppCompatActivity {
    final Fragment fragmentDashboard = new DashboardFragment();
    final Fragment fragmentTransaction = new TransactionFragment();
    final Fragment fragmentCategory = new CateogryFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentDashboard;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(active).show(fragmentDashboard).commit();
                    active = fragmentDashboard;
                    return true;
                case R.id.navigation_transaction:
                    fm.beginTransaction().hide(active).show(fragmentTransaction).commit();
                    active = fragmentTransaction;
                    return true;
                case R.id.navigation_category:
                    fm.beginTransaction().hide(active).show(fragmentCategory).commit();
                    active = fragmentCategory;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, fragmentCategory, "Category").hide(fragmentCategory).commit();
        fm.beginTransaction().add(R.id.main_container, fragmentTransaction, "Transaction").hide(fragmentTransaction).commit();
        fm.beginTransaction().add(R.id.main_container,fragmentDashboard, "Dashboard").commit();
    }

}
