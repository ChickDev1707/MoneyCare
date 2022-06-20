package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
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
import java.util.Date;
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
        // use wallet id to fetch instead of path because we store current wallet in preference by wallet id
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

    public void saveNewWallet(String name, Long money, String image, FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        CollectionReference groupsRef = db.collection("users").document(currentUserId).collection("wallets");
        Wallet wallet = new Wallet(null, name, money, image);
        groupsRef.add(wallet.toMap())
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
    public void updateWallet(Wallet newWallet, FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        DocumentReference walletRef = db.collection("users").document(currentUserId).collection("wallets").document(newWallet.id);
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                updateWalletMoney(transaction, walletRef, newWallet);
                transaction.update(walletRef, "name", newWallet.name);
                transaction.update(walletRef, "image", newWallet.image);
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

    private void updateWalletMoney(Transaction transaction, DocumentReference walletRef, Wallet newWallet) throws FirebaseFirestoreException {
        DocumentReference transactionsRef = db.collection("users").document(currentUserId).collection("transactions").document();
        DocumentSnapshot walletSnapshot = transaction.get(walletRef);
        Long walletMoney = walletSnapshot.getLong("money");
        UserTransaction newUserTrans = createModifiedTransaction(newWallet, walletMoney);

        transaction.update(walletRef, "money", newWallet.money);
        transaction.set(transactionsRef, newUserTrans.toMap());
    }
    private UserTransaction createModifiedTransaction(Wallet newWallet, Long walletMoney){
        String groupPath = newWallet.money> walletMoney? "transaction-groups/salary": "transaction-groups/eating";
        Long money = Math.abs(newWallet.money - walletMoney);
        String walletPath = getWalletPath(newWallet.id);
        return new UserTransaction(null, money, groupPath, "Điều chỉnh số dư", new Date(), walletPath, "");
    }

    public void deleteWallet(Wallet wallet, FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        DocumentReference walletRef = db.collection("users").document(currentUserId).collection("wallets").document(wallet.id);
        walletRef.delete()
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
    private String getWalletPath(String walletId){
        return String.format("users/%s/wallets/%s", currentUserId, walletId);
    }
}
