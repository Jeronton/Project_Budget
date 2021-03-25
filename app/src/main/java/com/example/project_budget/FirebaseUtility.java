package com.example.project_budget;

import android.app.Activity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;

/**
 * Provides firbase functionality for Project Budget.
 */
public class FirebaseUtility {
    private static FirebaseUtility instance;

    protected FirebaseAuth mFirebaseAuth;
    protected FirebaseDatabase mDatabase;

    private final int RC_AUTH = 1;


    private FirebaseUtility() {
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Gets the singleton instance of FirebaseUtility.
     * @return The instance of FirebaseUtility.
     */
    public static FirebaseUtility getInstance(){
        if (instance == null){
            instance = new FirebaseUtility();
        }
        return instance;
    }


    /**
     * Authenticates the user. If not logged in then will start login activity for result, using request code 1.
     * @param activity The activity to direct the login response to.
     */
    public void authenticate(Activity activity) {
        // authenticate to firebase:
        if (mFirebaseAuth.getCurrentUser() == null) {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    activity,
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_AUTH,
                    null);
        }
    }

    /**
     * Gets a Firebase DB reference for the path specified.
     * @param path The path in the database to get a reference to.
     * @return The database reference.
     */
    public DatabaseReference getDBReference(String path){
        return mDatabase.getReference(path);
    }

    /**
     * Gets the Firebase Auth.
     * @return The Firebase Auth.
     */
    public FirebaseAuth getmFirebaseAuth(){
        return this.mFirebaseAuth;
    }

    /**
     * Saves a transaction to the database.
     * @param transaction The transaction to save.
     * @param categoryIndex The index of the category to save transaction under.
     * @param transactionIndex The index of the transaction in transactions array for its category.
     *                         Should be 1 higher then the last transaction when adding a new transaction.
     */
    public void saveTransaction(Transaction transaction, Integer categoryIndex, Integer transactionIndex){

        DatabaseReference ref = mDatabase.getReference()
                .child("Categories")
                .child(categoryIndex.toString())
                .child("transactions")
                .child(transactionIndex.toString());
        ref.setValue(transaction);

    }



}
