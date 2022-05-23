package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Transaction;
import com.example.moneycare.ui.view.BudgetDetailFragment;
import com.example.moneycare.utils.DateUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionRepository {
    FirebaseFirestore db;

    public TransactionRepository(){
        db = FirebaseFirestore.getInstance();
    }
    public void fetchTransactions(FirestoreListCallback callback){
        CollectionReference colRef =  db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<Transaction> transactions = new ArrayList<Transaction>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        Transaction trans = Transaction.fromMap(snapshot.getData());
                        System.out.println(trans.money);
                        transactions.add(trans);
                    }
                    callback.onCallback(transactions);
                }
            }

        });
    }

    public void getTotalSpendByGroup(TransactionRepository.FirestoreCallback callback, Date startDate, String idGroup){
        Query  query = db.collection("users").document("LE3oa0LyuujvLqmvxoQw")
                .collection("transactions")
                .whereGreaterThan("date", startDate)
                .whereLessThanOrEqualTo("date", DateUtil.getLastDateOfMonth());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Long total = 0L;
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                       Transaction trans = Transaction.fromMap(snapshot.getData());
                        if(trans.group.equals(idGroup)){
                            Long itemMoney = trans.money;
                            total += itemMoney;
                        }
                    }
                    callback.onCallback(total);
                }
                else {
                    System.out.println("error" + task.getException());
                }
            }
        });
    }

    public interface FirestoreListCallback {
        public void onCallback(List<Transaction> transactions);
    }
    public interface FirestoreCallback<T> {
        public void onCallback(T t);
    }

}
