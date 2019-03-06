package com.example.budgetandexpense;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BudgetAndExpenseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Set up Realm database
        Realm.init(getApplicationContext());
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("budget_and_expense")
                .schemaVersion(1)
                .build());

    }
}
