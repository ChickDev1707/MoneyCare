package com.example.moneycare.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReportRepository {
    FirebaseFirestore db;
    String                currentUserId;
    public ReportRepository(){
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
