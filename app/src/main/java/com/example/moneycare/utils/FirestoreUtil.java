package com.example.moneycare.utils;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreUtil {
    public static DocumentReference getReferenceFromString(String path){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        return fs.document(path);
    }
}
