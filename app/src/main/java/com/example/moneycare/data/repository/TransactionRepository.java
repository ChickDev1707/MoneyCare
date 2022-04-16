package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.utils.DateUtil;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionRepository {
    FirebaseFirestore db;

    public TransactionRepository(){
        db = FirebaseFirestore.getInstance();
    }
    public void fetchYearTransactions(Date yearDate, FirestoreListCallback callback){
        CollectionReference colRef =  db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<UserTransaction> transactions = new ArrayList<UserTransaction>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        UserTransaction trans = UserTransaction.fromMap(snapshot.getData());
                        if(DateUtil.compareYear(yearDate, trans.date)) transactions.add(trans);
                    }
                    getGroupTransactionList(transactions, callback);
                }
            }

        });
    }
    public void fetchMonthTransactions(Date monthDate, FirestoreListCallback callback){
        CollectionReference colRef =  db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<UserTransaction> transactions = new ArrayList<UserTransaction>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        UserTransaction trans = UserTransaction.fromMap(snapshot.getData());
                        if(DateUtil.compareMonth(monthDate, trans.date)) transactions.add(trans);
                    }
                    getGroupTransactionList(transactions, callback);
                }
            }

        });
    }
    public void fetchDayTransactions(Date dayDate, FirestoreListCallback callback){
        CollectionReference colRef =  db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<UserTransaction> transactions = new ArrayList<UserTransaction>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        UserTransaction trans = UserTransaction.fromMap(snapshot.getData());
                        if(DateUtil.compareDate(dayDate, trans.date)) transactions.add(trans);
                    }
                    getGroupTransactionList(transactions, callback);
                }
            }

        });
    }
    public void fetchGroups(FirestoreListCallback callback){

        CollectionReference colRef =  db.collection("transaction-groups");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<Group> groups = new ArrayList<Group>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        Group group = Group.fromMap(snapshot.getId(), snapshot.getData());
                        groups.add(group);
                    }
                    callback.onCallback(groups);
                }
            }
        });
    }
    public void saveNewTrans(long money, Group group, String note, Date date){
        CollectionReference transactionsRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions");
        String groupPath = "transaction-groups/" + group.id;
        UserTransaction newTrans = new UserTransaction(money, groupPath, note, date, "wallets/wall-1");
        transactionsRef.add(newTrans.toMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("add trans success");
                        updateWallet("wall-1", money, group);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        System.out.println(e);
                    }
                });
    }

    public void updateWallet(String walletPath, long money, Group group){
        DocumentReference walletRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("wallets").document(walletPath);
        db.runTransaction(new Transaction.Function<Long>() {
            @Override
            public Long apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(walletRef);
                Long newMoney = group.type == true? snapshot.getLong("money") + money: snapshot.getLong("money") - money;
                transaction.update(walletRef, "money", newMoney);
                return newMoney;
            }
        }).addOnSuccessListener(new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                System.out.println("Update wallet success");
                System.out.println(result);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Update wallet failed");
            }
        });
    }

    private void getGroupTransactionList(List<UserTransaction> transactions, FirestoreListCallback callback){
        fetchGroups(groups->{
            List<GroupTransaction> groupTransactionList = new ArrayList<>();
            for(UserTransaction transaction:transactions){
                System.out.println(transaction.group);
                int pos = getGroupTransactionByGroupPath(groupTransactionList, transaction.group);
                if(pos == -1){
                    // not found
                    List<UserTransaction> newTransList = new ArrayList<>();
                    newTransList.add(transaction);
                    Group group = getGroup(groups, transaction.group);
                    GroupTransaction groupTrans = new GroupTransaction((Group) group, newTransList);
                    groupTransactionList.add(groupTrans);
                }else{
                    // found
                    GroupTransaction target = groupTransactionList.get(pos);
                    List<UserTransaction> newTransList = target.transactionList;
                    newTransList.add(transaction);
                    Group group = getGroup(groups, transaction.group);
                    GroupTransaction groupTrans = new GroupTransaction((Group) group, newTransList);
                    groupTransactionList.set(pos, groupTrans);
                }
            }
            callback.onCallback(groupTransactionList);
        });
    }
    private Group getGroup(List<Group> groups, String groupPath){
        for(Group group:groups){
            String path= "transaction-groups/"+ group.id;
            if(path.equals(groupPath)) return group;
        }
        return null;
    }
    private int getGroupTransactionByGroupPath(List<GroupTransaction> list, String groupPath){
        int i = 0;
        for(GroupTransaction groupTransaction:list){
            String path= "transaction-groups/"+ groupTransaction.group.id;
            if(path.equals(groupPath)) return i;
            i++;
        }
        return -1;
    }

}
