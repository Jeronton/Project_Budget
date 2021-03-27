package com.example.project_budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INDEX = "index";

    private final String TAG = "JG";

    private View view;

    private DataViewModel viewModel;

    private Category category;
    private int index;

    ListView transactionListView;
    TransactionsAdapter transactionsAdapter;
    ArrayList<Transaction> transactionsList;
    MainActivity mainActivity;

    Button btnAdd;
    TextView tvName;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param index The index of this category in the dataViewModel's list of categories.
     * @return A new instance of fragment CategoryFragment.
     */
    public static CategoryFragment newInstance(int index) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_INDEX);
            viewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
            // Get this category from the ViewModel
            category = viewModel.getCategories().getValue().get(index);
        }
        else {
            category = null;
        }

        mainActivity = (MainActivity)getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = getView();

        // get references
        tvName = view.findViewById(R.id.tv_category_name);
        btnAdd = view.findViewById(R.id.btn_add);

        tvName.setText(category.getName());

        // initialize the transaction list
        transactionsList = category.getTransactions();


        transactionsAdapter = new TransactionsAdapter(getContext(), R.layout.transaction_list_item, transactionsList);
        transactionListView = (ListView)view.findViewById(R.id.lv_transaction_list);

        //set the adapter of the ListView
        transactionListView.setAdapter(transactionsAdapter);

        //have listview respond to selected items
        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mainActivity.displayModifyTransaction(index, i);
//                Transaction currentItem = transactionsList.get(i);
//                Toast.makeText(getContext(), "Transaction Clicked! Price: " + currentItem.getAmount(), Toast.LENGTH_LONG).show();

            }
        });

        // set up add transaction listener:
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.displayAddTransaction(index);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    //region Transactions List View


    //endregion


}