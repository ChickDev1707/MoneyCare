package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TransactionGroupRepository {
    FirebaseFirestore db;

    public TransactionGroupRepository(){
        db = FirebaseFirestore.getInstance();
    }
    public void fetchTransactionGroups(FirestoreListCallback callback){
        CollectionReference colRef =  db.collection("transaction-groups");
        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Group> transactionGroups = new ArrayList<Group>();
                for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                    Group transGr = Group.fromMap(snapshot.getId(), snapshot.getData());
                    transactionGroups.add(transGr);
                }
                callback.onCallback(transactionGroups);
            }
        });
    }
}
