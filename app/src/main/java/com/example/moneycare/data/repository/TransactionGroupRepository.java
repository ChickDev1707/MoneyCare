package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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
    public void fetchTransactionGroups(FirestoreListCallback callback){
        CollectionReference colRef =  db.collection("transaction-groups");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                List<Group> transactionGroups = new ArrayList<Group>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        Group transGr = Group.fromMap(snapshot.getId(), snapshot.getData());
                        transactionGroups.add(transGr);
                    }
                    callback.onCallback(transactionGroups);
                }

            }

        });
    }
}
