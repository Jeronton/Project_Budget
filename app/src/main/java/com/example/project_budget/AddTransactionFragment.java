package com.example.project_budget;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTransactionFragment extends Fragment {

    private final String TAG = "JG";
    protected FragmentManager fragmentManager;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CAT_INDEX = "category_index";
    private static final String ARG_TRANS_INDEX = "transaction_index";

    private DataViewModel viewModel;

    protected FirebaseUtility firebaseUtility;

    private int category_index;
    private int  transaction_index;
    private boolean isModifyMode = false;

    private Transaction transaction;

    private EditText etAmount, etDate, etLocation, etDesc, etNotes;
    private Button btnAddTrans, btnCancel;
    ConstraintLayout clTransactionLayout;

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Gets an instance of the fragment in add transaction mode.
     * @param category_index The category_index of the category in the ViewModel list of categories.
     * @return A new instance of fragment AddTransactionFragment in add mode.
     */
    public static AddTransactionFragment newInstance(int category_index) {
        AddTransactionFragment fragment = new AddTransactionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CAT_INDEX, category_index);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Gets an instance of the fragment in update/view transaction mode.
     * @param category_index The category_index of the category in the ViewModel list of categories.
     * @param transaction_index The category_index of the transaction to be modified/viewed.
     * @return A new instance of fragment AddTransactionFragment in update/view mode.
     */
    public static AddTransactionFragment newInstance(int category_index, int transaction_index) {
        AddTransactionFragment fragment = new AddTransactionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CAT_INDEX, category_index);
        args.putInt(ARG_TRANS_INDEX, transaction_index);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category_index = getArguments().getInt(ARG_CAT_INDEX);
            transaction_index = getArguments().getInt(ARG_TRANS_INDEX, -1);
        }
        // Get References
        firebaseUtility = FirebaseUtility.getInstance();
        fragmentManager = getParentFragmentManager();
        viewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        if (transaction_index != -1) {
            // is in modify/view mode
            isModifyMode = true;
            Button btn = view.findViewById(R.id.btn_cancel);
            btn.setText(R.string.back_transaction_btn);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get references
        etAmount = view.findViewById(R.id.etAmount);
        etLocation = view.findViewById(R.id.et_transaction_location);
        etDate = view.findViewById(R.id.et_transaction_date);
        etDesc = view.findViewById(R.id.et_desc);
        etNotes = view.findViewById(R.id.et_notes);
        btnAddTrans = view.findViewById(R.id.btn_add_transaction);
        btnCancel = view.findViewById(R.id.btn_cancel);
        clTransactionLayout = view.findViewById(R.id.cl_transaction_layout);

        // if in modify/view mode then get transaction and populate edit texts
        if (isModifyMode){
            transaction = viewModel
                    .getCategories()
                    .getValue()
                    .get(category_index)
                    .getTransactions().get(transaction_index);
            Double amount = transaction.getAmount();
            etAmount.setText(amount.toString());
            etLocation.setText(transaction.getLocation());
            etDesc.setText(transaction.getDescription());
            etNotes.setText(transaction.getNotes());
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            etDate.setText(format.format(transaction.getDate()));
        }

        // set add transaction listener
        btnAddTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });

        // set cancel/back listener
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

        // close when clicked outside of popup area
        clTransactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });

    }

    /**
     * Adds a transaction to the db based on the edit text values.
     */
    private void addTransaction(){
        double amount = 0;
        String location = "";
        String desc = "";
        String notes = "";
        Date date = new Date();

        boolean valid = true;


        etAmount.setBackgroundColor(Color.WHITE);
        etAmount.setHint("");
        etNotes.setBackgroundColor(Color.WHITE);
        etNotes.setHint("");
        etDesc.setBackgroundColor(Color.WHITE);
        etDesc.setHint("");
        etDate.setBackgroundColor(Color.WHITE);
        etLocation.setBackgroundColor(Color.WHITE);
        etLocation.setHint("");

        try {
            if (etAmount.getText().toString().isEmpty()){
                throw new Exception("required");
            }
            else {
                amount = Double.parseDouble(etAmount.getText().toString());
            }
        }
        catch (Exception e){
            etAmount.setBackgroundColor(Color.RED);
            etAmount.setHint(R.string.required);
            valid = false;
        }

        try {
            if (etDate.getText().toString().isEmpty()){
                throw new Exception("required");
            }
            else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                date = format.parse(etDate.getText().toString());
            }
        }
        catch (Exception e){
            etDate.setBackgroundColor(Color.RED);
            etDate.setText("");
            valid = false;
        }

        location = etLocation.getText().toString();
        desc = etDesc.getText().toString();
        notes = etNotes.getText().toString();

        // create transaction
        if (valid) {
            Transaction trans = new Transaction(amount, desc, location, notes, date);
            // add transaction

            int position = isModifyMode ? transaction_index : viewModel.getCategories().getValue().get(category_index).getTransactions().size();
            firebaseUtility.saveTransaction(trans, category_index, position);
            closeFragment();
        }

    }

    /**
     * Closes this Transaction fragment performing any required work.
     */
    private void closeFragment(){
        getParentFragmentManager().popBackStack();
    }
}