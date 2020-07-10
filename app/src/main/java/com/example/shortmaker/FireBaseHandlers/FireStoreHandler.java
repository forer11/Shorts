package com.example.shortmaker.FireBaseHandlers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class FireStoreHandler {
    private static final String USERS = "Users";


    private FirebaseFirestore db;
    private CollectionReference usersRef;

    private Context context;

    public FireStoreHandler(Context context)
    {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(USERS);

    }

    public void createUserIfNotExists(FirebaseUser user) {
        if (user != null && user.getEmail() != null) {
            final DocumentReference userRef = usersRef.document(user.getEmail());
            userRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot == null || !documentSnapshot.exists()) {
                                userRef.set(new HashMap<String, Object>(), SetOptions.merge());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.v(TAG, "error loading user");
                        }
                    });
        }
    }

}
