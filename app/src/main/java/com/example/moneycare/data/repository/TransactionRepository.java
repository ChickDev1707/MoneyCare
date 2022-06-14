package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.utils.DateUtil;
import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.utils.FirestoreUtil;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
    // transactions actions
    public void fetchYearTransactions(Date yearDate, FirestoreListCallback<GroupTransaction> callback){
        CollectionReference colRef =  db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<UserTransaction> transactions = new ArrayList<UserTransaction>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        UserTransaction trans = UserTransaction.fromMap(snapshot.getId(), snapshot.getData());
                        if(DateUtil.compareYear(yearDate, trans.date)) transactions.add(trans);
                    }
                    getGroupTransactionList(transactions, callback);
                }
            }

        });
    }
    public void fetchMonthTransactions(Date monthDate, FirestoreListCallback<GroupTransaction> callback){
        CollectionReference colRef =  db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<UserTransaction> transactions = new ArrayList<UserTransaction>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        UserTransaction trans = UserTransaction.fromMap(snapshot.getId(), snapshot.getData());
                        if(DateUtil.compareMonth(monthDate, trans.date)) transactions.add(trans);
                    }
                    getGroupTransactionList(transactions, callback);
                }
            }
        });
    }
    public void fetchDayTransactions(Date dayDate, FirestoreListCallback<GroupTransaction> callback){
        CollectionReference colRef =  db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<UserTransaction> transactions = new ArrayList<UserTransaction>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        UserTransaction trans = UserTransaction.fromMap(snapshot.getId(), snapshot.getData());
                        if(DateUtil.compareDate(dayDate, trans.date)) transactions.add(trans);
                    }
                    getGroupTransactionList(transactions, callback);
                }
            }

        });
    }

    public void saveNewTransaction(long money, Group group, String note, Date date){
        CollectionReference transactionsRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions");
        String groupPath = "transaction-groups/" + group.id;
        UserTransaction newTrans = new UserTransaction(null, money, groupPath, note, date, "wallets/wall-1");
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
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Update wallet failed");
                    }
                });
    }
    public void deleteTransaction(UserTransaction transaction, Group group){
        DocumentReference docRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions").document(transaction.id);
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("delete trans success");
                        updateWallet("wall-1", transaction.money, group);
                    }
                });
    }
    // group transactions
    private void getGroupTransactionList(List<UserTransaction> transactions, FirestoreListCallback<GroupTransaction> callback){
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
    private int getGroupTransactionByGroupPath(List<GroupTransaction> list, String groupPath){
        int i = 0;
        for(GroupTransaction groupTransaction:list){
            String path= "transaction-groups/"+ groupTransaction.group.id;
            if(path.equals(groupPath)) return i;
            i++;
        }
        return -1;
    }
    public void updateTransaction(UserTransaction userTransaction, Group group, FirestoreObjectCallback<UserTransaction> callback){
        DocumentReference transactionRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transactions").document(userTransaction.id);
        String walletString = "users/LE3oa0LyuujvLqmvxoQw/" + userTransaction.wallet;
        DocumentReference walletRef = db.document(walletString);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot transactionSnapshot = transaction.get(transactionRef);
                DocumentSnapshot walletSnapshot = transaction.get(walletRef);

                Long oldMoney = transactionSnapshot.getLong("money");
                Long walletTotalMoney = walletSnapshot.getLong("money");
                Long newUpdatedMoney = getNewUpdatedMoney(walletTotalMoney, oldMoney, userTransaction.money, group.type);

                // update wallet
                transaction.update(walletRef, "money", newUpdatedMoney);

                transaction.update(transactionRef, "money", userTransaction.money);
                transaction.update(transactionRef, "group", FirestoreUtil.getReferenceFromString(userTransaction.group));
                transaction.update(transactionRef, "note", userTransaction.note);
                transaction.update(transactionRef, "date", userTransaction.date);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                System.out.println("Update transaction success");
                callback.onCallback(null);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Update transaction failed");
                    }
                });
    }
    private Long getNewUpdatedMoney(Long oldTotal, Long oldValue, Long newValue, boolean type){
        Long absDiff = Math.abs(oldValue - newValue);
        Long newMoney = type == true? oldTotal - absDiff: oldTotal+ absDiff;
        return newMoney;
    }
    // group actions
    public void fetchGroups(FirestoreListCallback<Group> callback){

        CollectionReference defaultGroupRef =  db.collection("transaction-groups");
        defaultGroupRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<Group> defaultGroups = getGroupsData(task, true);
                fetchUserGroups(defaultGroups, callback);
            }
        });

    }
    public void fetchUserGroups(List<Group> defaultGroups, FirestoreListCallback<Group> callback){
        CollectionReference userGroupRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transaction-groups");
        userGroupRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<Group> allGroups = new ArrayList<>(defaultGroups);
                List<Group> userGroups = getGroupsData(task, false);
                allGroups.addAll(userGroups);
                callback.onCallback(allGroups);
            }
        });
    }
    private Group getGroup(List<Group> groups, String groupPath){
        for(Group group:groups){
            String path= "transaction-groups/"+ group.id;
            if(path.equals(groupPath)) return group;
        }
        return null;
    }
    private List<Group> getGroupsData(Task<QuerySnapshot> task, boolean isDefault){
        List<Group> groups = new ArrayList<Group>();
        if(task.isSuccessful()){
            for(DocumentSnapshot snapshot:task.getResult()){
                Group group = Group.fromMap(snapshot.getId(), snapshot.getData());
                group.isDefault = isDefault;
                groups.add(group);
            }
        }
        return groups;
    }
    public void fetchGroup(String groupPath, FirestoreObjectCallback<Group> callback){
        DocumentReference groupRef =  db.document(groupPath);
        groupRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    Group group = Group.fromMap(snapshot.getId(), snapshot.getData());
                    callback.onCallback(group);
                }
            }
        });
    }
    public void saveNewGroup(String name, boolean type, String image){
        CollectionReference groupsRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transaction-groups");
        Group newGroup = new Group(null, name, type, image);
        groupsRef.add(newGroup.toMap())
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("add group success");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                System.out.println(e);
            }
        });
    }

    public void getTotalSpendByGroup(FirestoreObjectCallback callback, Date startDate, String idGroup){
        Query  query = db.collection("users").document("WSHF04um7aZW5wgzxn3ZDzI3kry1")
                .collection("transactions")
                .whereGreaterThan("date", startDate)
                .whereLessThanOrEqualTo("date", DateUtil.getLastDateOfMonth());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Long total = 0L;
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                       UserTransaction trans = UserTransaction.fromMap(snapshot.getId(),snapshot.getData());
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

    public void updateGroup(Group group){
        DocumentReference groupRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transaction-groups").document(group.id);
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(groupRef, "name", group.name);
                transaction.update(groupRef, "image", group.image);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                System.out.println("Update group success");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Update group failed");
            }
        });
    }
    public void deleteGroup(Group group){
        DocumentReference docRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("transaction-groups").document(group.id);
        docRef.delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("delete group success");
            }
        });
    }
    // wallet
    public void fetchWallets(FirestoreListCallback<Wallet> callback){
        CollectionReference walletsRef = db.collection("users").document("WSHF04um7aZW5wgzxn3ZDzI3kry1").collection("wallets");
        walletsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                List<Wallet> wallets = new ArrayList<Wallet>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        Wallet wallet = Wallet.fromMap(snapshot.getId(), snapshot.getData());
                        wallets.add(wallet);
                    }
                }
                callback.onCallback(wallets);
            }
        });

    }
    public void fetchWallet(String walletId, FirestoreObjectCallback<Wallet> callback){
        DocumentReference walletRef = db.collection("users").document("LE3oa0LyuujvLqmvxoQw").collection("wallets").document(walletId);
        walletRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Wallet wallet = Wallet.fromMap(task.getResult().getId(), task.getResult().getData());
                    callback.onCallback(wallet);
                }
            }
        });
    }
}
