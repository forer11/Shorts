package com.example.shortmaker.FireBaseHandlers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.shortmaker.DataClasses.Icon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class FireStoreHandler {
    private static final String USERS = "Users";
    private static final String ICONS = "Icons";


    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private CollectionReference iconsRef;

    private boolean iconsLoaded;

    private Context context;

    public FireStoreHandler(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(USERS);
        iconsRef = db.collection(ICONS);
        iconsLoaded = false;
    }

    public void createUserIfNotExists(FirebaseUser user) {
        if (user != null && user.getEmail() != null) {
            final DocumentReference userRef = usersRef.document(user.getEmail()
                    + user.getUid());
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

    public void loadIcons(final ArrayList<Icon> icons) {
        if (!iconsLoaded) {
            iconsRef.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Icon icon = snapshot.toObject(Icon.class);
                                icons.add(icon);
                            }
                            iconsLoaded = true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

}
