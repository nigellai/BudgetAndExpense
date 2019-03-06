package com.example.budgetandexpense.model;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;

public class Category extends RealmObject {

    public String name;
    public int colour;
    public double budget;

    /* insert Category into Realm data base */
    public static Category createCategory(String name, int colour, double budget) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Category category = realm.where(Category.class).equalTo("name", name).findFirst();

        if (category == null) {
            category = realm.createObject(Category.class);
            category.name = name;
        }
        category.colour = colour;
        category.budget = budget;
        realm.commitTransaction();
        return category;
    }

    /* enquery all Category from Realm data base */
    public static List<Category> getAllCategories() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Category> query = realm.where(Category.class);
        return query.findAll();
    }

    /* calculate whether the category budget exceeded */
    public static boolean ifExceeded(String categoryName) {
        Realm realm = Realm.getDefaultInstance();
        Category category = realm.where(Category.class).equalTo("name", categoryName).findFirst();
        List<Transaction> relatedTransaction = realm.where(Transaction.class).equalTo("categoryName", categoryName).findAll();
        double sum = 0;
        for(Transaction transaction: relatedTransaction) {
            sum += transaction.value;
        }
        return sum > category.budget;
    }

}
