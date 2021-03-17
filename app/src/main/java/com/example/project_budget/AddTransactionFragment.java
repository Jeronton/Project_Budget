package com.example.project_budget;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTransactionFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INDEX = "index";

    protected FirebaseAuth mFirebaseAuth;
    protected FirebaseDatabase mDatabase;

    private int index;

    private EditText etAmount, etDate, etLocation, etDesc, etNotes;
    private Button btnAddTrans;

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param index The index of the category in the ViewModel list of categories.
     * @return A new instance of fragment AddTransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTransactionFragment newInstance(int index) {
        AddTransactionFragment fragment = new AddTransactionFragment();
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
        }
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_transaction, container, false);
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

        // set add transaction listener
        btnAddTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });

    }

    /**
     * Adds a transaction to the db based on the
     */
    private void addTransaction(){
        float amount = 0;
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
                amount = Float.parseFloat(etAmount.getText().toString());
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
                SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd");
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

        }
    }
}