package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.TransactionGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TransactionGroupRepository {
    FirebaseFirestore db;

    public TransactionGroupRepository(){
        db = FirebaseFirestore.getInstance();
    }
    public void fetchTransactionGroups(TransactionGroupRepository.FirestoreCallback callback){

        CollectionReference colRef =  db.collection("transaction-groups");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                List<TransactionGroup> transactionGroups = new ArrayList<TransactionGroup>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        TransactionGroup transGr = TransactionGroup.fromMap(snapshot.getData());
                        transGr.setId(snapshot.getId());
                        transactionGroups.add(transGr);
                    }
                    callback.onCallback(transactionGroups);
                }

            }

        });
    }

    public interface FirestoreCallback {
        public void onCallback(List<TransactionGroup> transGr);
    }
}
