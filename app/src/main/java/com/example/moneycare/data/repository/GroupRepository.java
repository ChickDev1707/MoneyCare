package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.utils.DateTimeUtil;
import com.example.moneycare.utils.FirestoreUtil;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GroupRepository {
    FirebaseFirestore db;
    String currentUserId;

    public GroupRepository(){
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
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
        CollectionReference userGroupRef = db.collection("users").document(currentUserId).collection("transaction-groups");
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
    public void saveNewGroup(String name, boolean type, String image, FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        CollectionReference groupsRef = db.collection("users").document(currentUserId).collection("transaction-groups");
        Group newGroup = new Group(null, name, type, image);
        groupsRef.add(newGroup.toMap())
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                successCallback.onCallback(null);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                failureCallback.onCallback(null);
            }
        });
    }
    public void updateGroup(Group group, FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        DocumentReference groupRef = db.collection("users").document(currentUserId).collection("transaction-groups").document(group.id);
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
                successCallback.onCallback(null);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureCallback.onCallback(null);
            }
        });
    }
    public void deleteGroup(Group group, FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        DocumentReference docRef = db.collection("users").document(currentUserId).collection("transaction-groups").document(group.id);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteTransactionsOfGroup(group);
                successCallback.onCallback(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureCallback.onCallback(null);
            }
        });
    }
    private void deleteTransactionsOfGroup(Group group){
        CollectionReference colRef =  db.collection("users").document(currentUserId).collection("transactions");
        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                List<UserTransaction> transactions = new ArrayList<UserTransaction>();
                for (DocumentSnapshot doc : snapshot.getDocuments()) {
                    UserTransaction transaction = UserTransaction.fromMap(doc.getId(), doc.getData());
                    String transGroupId = FirestoreUtil.getReferenceFromPath(transaction.group).getId();
                    if (group.id.equals(transGroupId)) transactions.add(transaction);
                }
                deleteTransactions(group, transactions, null);
            }
        });

    }
    private void deleteTransactions(Group group, List<UserTransaction> transactions, FirestoreObjectCallback<Void> callback){
        for (int i = 0; i<transactions.size(); i++){
            UserTransaction trans = transactions.get(i);
            deleteTransaction(group, trans);
        }
    }
    private void deleteTransaction(Group group, UserTransaction userTransaction){
        DocumentReference docRef = db.collection("users").document(currentUserId).collection("transactions").document(userTransaction.id);
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference walletRef = FirestoreUtil.getReferenceFromPath(userTransaction.wallet);
                DocumentSnapshot walletSnapshot = transaction.get(walletRef);

                Long walletMoney = walletSnapshot.getLong("money");
                walletMoney = group.type? walletMoney - userTransaction.money: walletMoney + userTransaction.money;

                transaction.update(walletRef, "money", walletMoney);
                transaction.delete(docRef);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("delete");
            }
        });
    }

}
