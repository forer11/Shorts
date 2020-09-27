package com.example.shortmaker.FireBaseHandlers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.shortmaker.DataClasses.Icon;
import com.example.shortmaker.DataClasses.Shortcut;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private static final String SHORTCUTS = "Shortcuts";
    public static final String DEFAULT_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/" +
            "shortmaker-dbb76.appspot.com/o/icons%2Fshortcut.png?alt=media&token=73eea0f1-e6a8-" +
            "4057-b6e8-4098e9b56c14";


    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private CollectionReference iconsRef;

    private String userKey;

    private boolean iconsLoaded;

    private Context context;

    public FireStoreHandler(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(USERS);
        iconsRef = db.collection(ICONS);
        iconsLoaded = false;
    }

    public void setUserKey(FirebaseUser user) {
        userKey = user.getEmail() + user.getUid();
    }

    public String getUserKey() {
        return userKey;
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

    public void loadShortcuts(final FireStoreCallback callback) {
        final ArrayList<Shortcut> shortcuts = new ArrayList<>();
        usersRef.document(userKey).collection(SHORTCUTS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            shortcuts.add(snapshot.toObject(Shortcut.class));
                        }
                        callback.onCallBack(shortcuts, true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallBack(shortcuts, false);
                    }
                });

    }

    public void addShortcut(final Shortcut shortcut, final SingleShortcutCallback callback) {
        usersRef.document(userKey).collection(SHORTCUTS).add(shortcut)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        shortcut.setId(id);
                        usersRef.document(userKey).collection(SHORTCUTS)
                                .document(id)
                                .set(shortcut, SetOptions.merge());
                        callback.onAddedShortcut(id, shortcut, true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onAddedShortcut(null, null, false);
                    }
                });
    }

    public void updateShortcut(Shortcut shortcut) {
        usersRef.document(userKey).collection(SHORTCUTS).document(shortcut.getId())
                .set(shortcut, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //TODO
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO
                    }
                });
    }

    public void getShortcut(String id, final SingleShortcutCallback callback) {
        usersRef.document(userKey).collection(SHORTCUTS).document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Shortcut shortcut = documentSnapshot.toObject(Shortcut.class);
                        if (shortcut != null) {
                            callback.onAddedShortcut(shortcut.getId(), shortcut, true);
                        } else {
                            callback.onAddedShortcut(null, null, false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onAddedShortcut(null, null, false);
                    }
                });
    }

    public interface FireStoreCallback {
        void onCallBack(ArrayList<Shortcut> shortcutsList, Boolean success);
    }

    public interface SingleShortcutCallback {
        void onAddedShortcut(String id, Shortcut shortcut, Boolean success);
    }


}
