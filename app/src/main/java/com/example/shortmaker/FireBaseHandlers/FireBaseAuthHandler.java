package com.example.shortmaker.FireBaseHandlers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseAuthHandler {
    private static final String WEB_ID = "354049566538-skh0gq6hkvv53rh1udmpb37obusmivrf.apps.googleusercontent.com";
    private Context context;
    public FirebaseUser user = null;
    public FirebaseAuth firebaseAuth;
    public GoogleSignInClient googleSignInClient;


    public FireBaseAuthHandler(Context context) {
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
        configureGoogleSignIn();
        userStateListener();
    }

    private void userStateListener() {
        final FirebaseAuth.IdTokenListener userListener = new FirebaseAuth.IdTokenListener() {
            @Override
            public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = FirebaseAuth.getInstance().getCurrentUser();
            }
        };
        firebaseAuth.addIdTokenListener(userListener);
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_ID)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }
}
