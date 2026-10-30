package com.example.agriecommerce.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtils {

    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final FirebaseStorage firebaseStore = FirebaseStorage.getInstance();

    // Get FirebaseAuth instance
    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    // Get FirebaseUser instance
    public static FirebaseUser getFirebaseUser() {
        return firebaseAuth.getCurrentUser();
    }

    // Get FirebaseDatabase instance
    public static FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    // Get FirebaseStorage instance
    public static FirebaseStorage getFirebaseStorage() {
        return firebaseStore;
    }

    // Get Firebase Storage Reference
    public static StorageReference getStorageReference() {
        return firebaseStore.getReference();
    }
}
