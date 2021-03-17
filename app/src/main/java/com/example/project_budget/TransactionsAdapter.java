package com.example.project_budget;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionsAdapter extends ArrayAdapter<Transaction> {

    private ArrayList<Transaction> items;

    public TransactionsAdapter(Context context, int textViewResourceId, ArrayList<Transaction> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    //This method is called once for every item in the ArrayList as the list is loaded.
    //It returns a View -- a list item in the ListView -- for each item in the ArrayList
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.transaction_list_item, null);
        }
        Transaction transaction = items.get(position);
        if (transaction != null) {
            TextView amount = (TextView) view.findViewById(R.id.tv_trans_amount);

            TextView date = (TextView) view.findViewById(R.id.tv_trans_date);

            if (amount != null) {
                amount.setText(String.valueOf(transaction.getAmount()));
            }
            if (date != null && transaction.getDate() != null) {
                SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
                date.setText(format.format(transaction.getDate()));
            }

        }
        return view;
    }
}
