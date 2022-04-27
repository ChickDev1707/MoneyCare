package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.Transaction;
import com.example.moneycare.utils.DateUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    FirebaseFirestore db;

    public TransactionRepository(){
        db = FirebaseFirestore.getInstance();
    }
    public void fetchTransactions(FirestoreCallback callback){

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

//        public void fetchTransactionByDate(BudgetRepository.FirestoreCallback callback, LocalDate startDate){
//        Query query =  db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions")
//                .whereGreaterThanOrEqualTo("date", startDate)
//                .whereLessThanOrEqualTo("date", DateUtil.getLastDateOfMonth())
//                .whereEqualTo("group", "")
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
//                List<Budget> budgets = new ArrayList<Budget>();
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot snapshot:task.getResult()){
//                        Budget budget = Budget.fromMap(snapshot.getData());
//                        budget.setId(snapshot.getId());
//                        budgets.add(budget);
//                    }
//                    callback.onCallbackList(budgets);
//                }
//            }
//        });
//    }
    public interface FirestoreCallback {
        public void onCallback(List<Transaction> transactions);
    }

}
