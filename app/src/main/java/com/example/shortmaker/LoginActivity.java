package com.example.shortmaker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private Button googleSignInButton, anonymousSignInButton;
    private GoogleSignInClient googleSignInClient;
    private static final String EMAIL = "email";
    private String TAG = "LoginActivity";
    private int RC_SIGN_IN = 1;

    AppData appData;
    FirebaseAuth firebaseAuth;
    private Button facebookSignIn;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getAppData();
        setViews();
        loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        setSignInClickListener();
        googleSignInClient = appData.fireBaseAuthHandler.googleSignInClient;

        callbackManager = CallbackManager.Factory.create();

    }

    private void setSignInClickListener() {
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInButton.setEnabled(false);
                signIn();
            }
        });

        facebookSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSignIn.setEnabled(false);
                loginButton.performClick();
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                signInWithFacebook(loginResult);
                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                            }
                        });
            }
        });
    }

    private void signInWithFacebook(LoginResult loginResult) {
        AuthCredential credential = FacebookAuthProvider
                .getCredential(loginResult.getAccessToken().getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            goToMainScreen();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void setViews() {
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        facebookSignIn = findViewById(R.id.facebook_sign_in_button);
    }

    private void getAppData() {
        appData = (AppData) getApplicationContext();
        firebaseAuth = appData.fireBaseAuthHandler.firebaseAuth;
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            handelSignInResults(data);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        googleSignInButton.setEnabled(true);
        facebookSignIn.setEnabled(true);
    }

    private void handelSignInResults(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(TAG, "firebaseAuthWithGoogle:" +
                    Objects.requireNonNull(account).getId());
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e);
            // ...
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            goToMainScreen();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void goToMainScreen() {
        Intent intent = new Intent(getApplicationContext(), ActivateBluetoothActivity.class);
        startActivity(intent);
        finish();
    }


}
