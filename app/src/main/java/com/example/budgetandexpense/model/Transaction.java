package com.example.budgetandexpense.model;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class Transaction extends RealmObject {
    public Date transactionDate;
    public double value;
    public String categoryName;

    /* Insert Transaction into Realm */
    public static Transaction createTrasaction(Date transactionDate, double value, String categoryName) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Transaction transaction = realm.where(Transaction.class)
                .equalTo("categoryName", categoryName)
                .and()
                .equalTo("value", value)
                .and()
                .equalTo("transactionDate", transactionDate)
                .findFirst();

        if (transaction == null) {
            transaction = realm.createObject(Transaction.class);
        }
        transaction.transactionDate = transactionDate;
        transaction.value = value;
        transaction.categoryName = categoryName;
        realm.commitTransaction();
        return transaction;
    }

    /* Get All transaction from Realm */
    public static List<Transaction> getAllTransactions() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Transaction> query = realm.where(Transaction.class);
        return query.findAll();
    }

    /* delete selected Transaction from Realm */
    public static void deleteTransaction(Transaction transaction) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Transaction> result = realm.where(Transaction.class).equalTo("categoryName", transaction.categoryName)
                .and()
                .equalTo("value", transaction.value)
                .and()
                .equalTo("transactionDate", transaction.transactionDate)
                .findAll();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }
}
