package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.TransactionGroup;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.utils.DateUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
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

    public void fetchBudgetsInMonth(BudgetRepository.FirestoreCallback callback){
        Query query =  db.collection("budgets")
                .whereGreaterThanOrEqualTo("date", DateUtil.getFirstDateOfMonth())
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

    public void fetchGroupByPath(FirestoreObjectCallback callback, String docPath){
        DocumentReference docRef = db.document(docPath);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                TransactionGroup group = TransactionGroup.fromMap(documentSnapshot.getData());
                group.setId(documentSnapshot.getId());
                callback.onCallback(group);
            }
        });
    }

    public void fetchBudgetById(FirestoreObjectCallback callback, String id){
        DocumentReference docRef = db.collection("budgets").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Budget budget = Budget.fromMap(documentSnapshot.getData());
                budget.setId(documentSnapshot.getId());
                callback.onCallback(budget);
            }
        });
    }

    public void insertBudget(Budget budget){
        db.collection("budgets")
                .add(budget.toMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("Insert successfully!!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document:" + e);
                    }
                });
    }

    public void updateBudget(String id, Long money){
        db.collection("budgets").document(id)
            .update("budgetOfMonth", money)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    System.out.println("DocumentSnapshot successfully updated!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Error updating document" +  e);
                }
            });
    }

    public void deleteBudget(String idBudget){
//        DocumentReference docRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("budgets").document(idBudget);
        DocumentReference docRef = db.collection("budgets").document(idBudget);
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("delete budgets success");
                    }
                });
    }

    public interface FirestoreCallback<T> {
        public void onCallback(List<T> list);
    }
    public interface FirestoreMultiCallback<T> {
        public void onCallback(List<T> list1, List<T> list2);
    }
    public interface FirestoreObjectCallback<T> {
        public void onCallback(T object);
    }
}
