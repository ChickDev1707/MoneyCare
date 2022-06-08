package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.utils.DateUtil;
import com.example.moneycare.utils.FirestoreUtil;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    String currentUserId;
    public TransactionRepository(){
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    // transactions actions
    public void fetchYearTransactions(Date yearDate, FirestoreListCallback<GroupTransaction> callback){
        CollectionReference colRef =  db.collection("users").document(currentUserId).collection("transactions");
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
        CollectionReference colRef =  db.collection("users").document(currentUserId).collection("transactions");
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
        CollectionReference colRef =  db.collection("users").document(currentUserId).collection("transactions");
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

    public void saveNewTransaction(long money, Group group, String note, Date date, String walletId, FirestoreObjectCallback<Void> callback){
        DocumentReference transactionsRef = db.collection("users").document(currentUserId).collection("transactions").document();
        String groupPath = getGroupPath(group);
        String walletPath = getWalletPath(walletId);
        UserTransaction newTrans = new UserTransaction(null, money, groupPath, note, date, walletPath);

        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference walletRef = FirestoreUtil.getReferenceFromPath(walletPath);
                DocumentSnapshot walletSnapshot = transaction.get(walletRef);

                Long walletMoney = walletSnapshot.getLong("money");
                walletMoney = group.type? walletMoney + money: walletMoney - money;

                transaction.update(walletRef, "money", walletMoney);
                transaction.set(transactionsRef, newTrans.toMap());
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onCallback(null);
            }
        });
    }
    public void updateWalletInAddTrans(String walletPath, long money, Group group, FirestoreObjectCallback<Void> callback){
        DocumentReference walletRef = db.collection("users").document(currentUserId).collection("wallets").document(walletPath);
        db.runTransaction(new Transaction.Function<Long>() {
            @Override
            public Long apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(walletRef);
                Long newMoney = group.type ? snapshot.getLong("money") + money: snapshot.getLong("money") - money;
                transaction.update(walletRef, "money", newMoney);
                return newMoney;
            }
        }).addOnSuccessListener(new OnSuccessListener<Long>() {
            @Override
            public void onSuccess(Long result) {
                System.out.println("Update wallet success");
                callback.onCallback(null);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Update wallet failed");
            }
        });
    }
    public void deleteTransaction(UserTransaction userTransaction, FirestoreObjectCallback<Void> callback){
        DocumentReference docRef = db.collection("users").document(currentUserId).collection("transactions").document(userTransaction.id);
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference walletRef = FirestoreUtil.getReferenceFromPath(userTransaction.wallet);
                DocumentSnapshot walletSnapshot = transaction.get(walletRef);
                DocumentSnapshot groupSnapshot = transaction.get(FirestoreUtil.getReferenceFromPath(userTransaction.group));

                Long walletMoney = walletSnapshot.getLong("money");
                boolean groupType = groupSnapshot.getBoolean("type");
                walletMoney = groupType? walletMoney - userTransaction.money: walletMoney + userTransaction.money;

                transaction.update(walletRef, "money", walletMoney);
                transaction.delete(docRef);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callback.onCallback(null);
            }
        });
    }
    // group transactions
    private void getGroupTransactionList(List<UserTransaction> transactions, FirestoreListCallback<GroupTransaction> callback){
        GroupRepository groupRepository = new GroupRepository();
        groupRepository.fetchGroups(groups->{
            List<GroupTransaction> groupTransactionList = new ArrayList<>();
            for(UserTransaction transaction:transactions){
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
    public Group getGroup(List<Group> groups, String groupPath){
        for(Group group:groups){
            String path= getGroupPath(group);
            if(path.equals(groupPath)) return group;
        }
        return null;
    }
    private int getGroupTransactionByGroupPath(List<GroupTransaction> list, String groupPath){
        int i = 0;
        for(GroupTransaction groupTransaction:list){
            String path= getGroupPath(groupTransaction.group);
            if(path.equals(groupPath)) return i;
            i++;
        }
        return -1;
    }
    public void updateTransaction(UserTransaction userTransaction, Group group, FirestoreObjectCallback<UserTransaction> callback){
        DocumentReference transactionRef = db.collection("users").document(currentUserId).collection("transactions").document(userTransaction.id);
        DocumentReference walletRef = db.document(userTransaction.wallet);
        DocumentReference groupRef = db.document(userTransaction.group);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot transactionSnapshot = transaction.get(transactionRef);
                DocumentSnapshot walletSnapshot = transaction.get(walletRef);
                DocumentSnapshot groupSnapshot = transaction.get(groupRef);

                Long oldTransMoney = transactionSnapshot.getLong("money");
                boolean oldGroupType = groupSnapshot.getBoolean("type");
                Long walletMoney = walletSnapshot.getLong("money");

                // compensate money
                walletMoney = oldGroupType? walletMoney - oldTransMoney: walletMoney + oldTransMoney;
                // update with new value
                walletMoney = group.type? walletMoney + userTransaction.money: walletMoney - userTransaction.money;

                // update wallet
                transaction.update(walletRef, "money", walletMoney);

                transaction.update(transactionRef, "money", userTransaction.money);
                transaction.update(transactionRef, "group", FirestoreUtil.getReferenceFromPath(getGroupPath(group)));
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
    private String getWalletPath(String walletId){
        return String.format("users/%s/wallets/%s", currentUserId, walletId);
    }
    private String getGroupPath(Group group){
        if(group.isDefault){
            return String.format("transaction-groups/" + group.id);
        }else return String.format("users/%s/transaction-groups/%s", currentUserId, group.id);
    }

}
