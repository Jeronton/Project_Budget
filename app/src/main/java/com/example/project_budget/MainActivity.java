package com.example.project_budget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "JG";
    protected FragmentManager fragmentManager;
//    protected FirebaseAuth mFirebaseAuth;
//    protected FirebaseDatabase mDatabase;
    protected FirebaseUtility firebaseUtility;

    private final int RC_AUTH = 1;

    private DataViewModel viewModel;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get References
        fragmentManager = getSupportFragmentManager();
//        mDatabase = FirebaseDatabase.getInstance();
//        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUtility = FirebaseUtility.getInstance();

        // initalize DataViewModel
        viewModel = new ViewModelProvider(this).get(DataViewModel.class);

//        // authenticate to firebase:
//        if (mFirebaseAuth.getCurrentUser() == null) {
//            // Choose authentication providers
//            List<AuthUI.IdpConfig> providers = Arrays.asList(
//                    new AuthUI.IdpConfig.EmailBuilder().build(),
//                    new AuthUI.IdpConfig.GoogleBuilder().build());
//
//            // Create and launch sign-in intent
//            startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setAvailableProviders(providers)
//                            .build(),
//                    RC_AUTH);
//        }
        firebaseUtility.authenticate(this);

        ArrayList<Category> categories = createDummieCategories();


        // write to db
        DatabaseReference myRef = firebaseUtility.getDBReference("Categories");
        myRef.push();
        findViewById(R.id.testWrite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the database
//                DatabaseReference myRef = mDatabase.getReference("message");
                myRef.setValue(categories);
//                myRef.push();

//                mDatabase.getReference().child("Test").push().setValue("Test Write");


                Toast.makeText(MainActivity.this, "Write", Toast.LENGTH_SHORT).show();
            }
        });


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                dataSnapshot.getValue()
//                Log.d(TAG, "Value is: " + value);
//                Category cat = ((ArrayList<Category>)dataSnapshot.getValue()).get(1);

                ArrayList test = (ArrayList)dataSnapshot.getValue();
//                HashMap<String, Object> map = (HashMap<String, Object>)test.get(1);

                viewModel.parseCategories((ArrayList)dataSnapshot.getValue());
//                viewModel.setCategories((ArrayList<Category>)dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        displayCategoriesView();


    }

    /**
     * Displays the add category fragment
     */
    public void displayAddCategory(){

    }

    /**
     * Displays the categories view fragment
     */
    public void displayCategoriesView(){
        fragmentManager.popBackStack();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true)
                .addToBackStack(null)
                .add(R.id.fragment_container_view, CategoriesViewFragment.class, null, "CategoryView")
                .commit();
    }

    /**
     * Displays the add transction fragment
     * @param categoryIndex The index of the category to add to.
     */
    public void displayAddTransaction(int categoryIndex){
        AddTransactionFragment frag = AddTransactionFragment.newInstance(categoryIndex);
//        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .add(R.id.fragment_container_view, frag, "AddTransaction")
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_AUTH) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = firebaseUtility.getmFirebaseAuth().getCurrentUser();
                Toast.makeText(this,"Logged in!", Toast.LENGTH_LONG).show();
                // ...

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Log in FAILED!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private ArrayList<Category> createDummieCategories(){
        ArrayList<Category> categories = new ArrayList<Category>(10);

        categories.add(new Category("Empty Category ", new ArrayList<Transaction>(0)));
        for(int i = 1; i <= 5; i++){
            ArrayList<Transaction> transactions = new ArrayList<Transaction>(10);
            for(int x = 1; x < 5; x++){
                Transaction t = new Transaction( i*10 + x + 0.75, "Transaction " + x, "Test Transaction", "Notes", new Date());
                transactions.add(t);
            }

            Category cat = new Category("Category " + i, transactions);
            categories.add(cat);
        }

        return categories;
    }


}