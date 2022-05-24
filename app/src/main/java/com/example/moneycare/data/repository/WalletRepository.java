package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.Wallet;
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

public class WalletRepository {
    FirebaseFirestore db;
    String currentUserId;
    public WalletRepository(){
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    // wallet
    public void fetchWallets(FirestoreListCallback<Wallet> callback){
        CollectionReference walletsRef = db.collection("users").document(currentUserId).collection("wallets");
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
        DocumentReference walletRef = db.collection("users").document(currentUserId).collection("wallets").document(walletId);
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
    public void fetchFirstWallet(FirestoreObjectCallback<Wallet> callback){
        CollectionReference walletsRef = db.collection("users").document(currentUserId).collection("wallets");
        walletsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult().getDocuments().get(0);
                    Wallet wallet = Wallet.fromMap(snapshot.getId(), snapshot.getData());
                    callback.onCallback(wallet);
                }
            }
        });
    }

    public void saveNewWallet(String name, Long money, String image){
        CollectionReference groupsRef = db.collection("users").document(currentUserId).collection("wallets");
        Wallet wallet = new Wallet(null, name, money, image);
        groupsRef.add(wallet.toMap())
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("add wallet success");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                System.out.println(e);
            }
        });
    }
    public void updateWallet(Wallet newWallet){
        DocumentReference walletRef = db.collection("users").document(currentUserId).collection("wallets").document(newWallet.id);
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(walletRef, "name", newWallet.name);
                transaction.update(walletRef, "image", newWallet.image);
                transaction.update(walletRef, "money", newWallet.money);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
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
    public void deleteWallet(Wallet wallet){
        DocumentReference walletRef = db.collection("users").document(currentUserId).collection("wallets").document(wallet.id);
        walletRef.delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("delete wallet success");
            }
        });
    }
}
