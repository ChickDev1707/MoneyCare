package com.example.moneycare.data.repository;

import com.example.moneycare.data.model.User;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRepository {
    FirebaseFirestore db;

    public LoginRepository(){
        db = FirebaseFirestore.getInstance();
    }
    public void createNewUser(FirebaseUser user, Wallet wallet, FirestoreObjectCallback<String> callback){
        String userId = user.getUid();
        User dbUser = new User(user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString());
        db.collection("users").document(userId).set(dbUser.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                createInitWallet(userId, wallet, callback);
            }
        });
    }
    private void createInitWallet(String userId, Wallet wallet, FirestoreObjectCallback<String> callback){
        CollectionReference walletsRef = db.collection("users").document(userId).collection("wallets");
        walletsRef.add(wallet.toMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                callback.onCallback(documentReference.getId());
            }
        });
    }
}