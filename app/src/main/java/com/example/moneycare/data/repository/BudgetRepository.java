package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.TransactionGroup;
import com.example.moneycare.utils.DateUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.List;

public class BudgetRepository {
    FirebaseFirestore db;

    public BudgetRepository(){
        db = FirebaseFirestore.getInstance();
    }
//    public void fetchBudgetById(BudgetRepository.FirestoreCallback callback,String id){
//        DocumentReference docRef = db.collection("budgets").document(id);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Budget budget = Budget.fromMap(document.getData());
//                        System.out.println(budget.getGroup_id());
//                        callback.onCallback(budget);
//                    }
//                }
//            }
//        });
//    }

    public void fetchBudgetsInMonth(BudgetRepository.FirestoreCallback callback){
        Query query =  db.collection("budgets").whereGreaterThanOrEqualTo("date", DateUtil.getFirstDateOfMonth())
                .whereLessThanOrEqualTo("date", DateUtil.getLastDateOfMonth());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                List<Budget> budgets = new ArrayList<Budget>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        Budget budget = Budget.fromMap(snapshot.getData());
                        budget.setId(snapshot.getId());
                        budgets.add(budget);
                    }
                    callback.onCallback(budgets);
                }
            }
        });
    }

    public interface FirestoreCallback {
        public void onCallback(List<Budget> budgets);
    }
}
