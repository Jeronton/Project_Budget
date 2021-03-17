package com.example.project_budget;

import android.util.Log;

import java.util.Date;
import java.util.HashMap;

// Describes a transaction.
public class Transaction {
    private double amount;
    private String description;
    private String location;
    private String notes;
    private Date date;
    private String id;


    public Transaction(double amount, String description, String location, String notes, Date date) {
        this.amount = amount;
        this.description = description;
        this.location = location;
        this.notes = notes;
        this.date = date;
//        this.id = id;
    }

    public Transaction(double amount, Date date){
        this(amount, null, null, null, date);
    }

    public static Transaction parseTransaction(HashMap<String, Object> transaction){
        Transaction parsedTransaction;
        try{
            double amount = (Double)transaction.get("amount");
            String description = (String)transaction.get("description");
            String location = (String)transaction.get("location");
            String notes = (String)transaction.get("notes");
            HashMap<String, Object> dateHash = (HashMap<String, Object>)transaction.get("date");
            Date date = new Date((long)dateHash.get("time"));

            parsedTransaction = new Transaction(
                    amount,
                    description,
                    location,
                    notes,
                    date
            );
        }
        catch (Exception e){
            Log.e("JG", "parseTransaction: Error occurred while parsing Transaction: " + e.toString());
            return  null;
        }
        return  parsedTransaction;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
