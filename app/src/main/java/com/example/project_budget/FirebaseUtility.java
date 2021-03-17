package com.example.project_budget;

import android.app.Activity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class FirebaseUtility {
    protected FirebaseAuth mFirebaseAuth;
    protected FirebaseDatabase mDatabase;

    private final int RC_AUTH = 1;

    public FirebaseUtility() {
        mDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();


    }



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


    public DatabaseReference getDBReference(String path){
        return mDatabase.getReference(path);
    }



}
