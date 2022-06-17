package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.Group;
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
        docRef.delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                successCallback.onCallback(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failureCallback.onCallback(null);
            }
        });
    }
}
