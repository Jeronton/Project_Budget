package com.example.project_budget;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Category {
    private final String TAG = "JG";

    private String name;
    private ArrayList<Transaction> transactions;

    public Category(String name, ArrayList<Transaction> transactions){

        this.name = name;
        this.transactions = transactions;
    }

    public Category(String name){
        this(name, null);
    }

    public static Category parseCategory(HashMap<String, Object> hash){
        String name;
        ArrayList<Transaction> transactions = new ArrayList<Transaction>(20);
        Category category;
        try {
            // parse the category name
            name = (String)hash.get("name");

            // parse the transactions
            if (hash.get("transactions") != null) {
                for (HashMap<String, Object> transaction : (ArrayList<HashMap<String, Object>>) hash.get("transactions")) {
                    transactions.add(Transaction.parseTransaction(transaction));
                }
            }

            category = new Category(name, transactions);
        }
        catch (Exception e){
            Log.e("JG", "parseCategory: Error parsing Category: " + e.toString());
            return null;
        }
        return category;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
