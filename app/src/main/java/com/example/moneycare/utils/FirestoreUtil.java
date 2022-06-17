package com.example.moneycare.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreUtil {
    public static DocumentReference getReferenceFromPath(String path){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        return fs.document(path);
    }
}
