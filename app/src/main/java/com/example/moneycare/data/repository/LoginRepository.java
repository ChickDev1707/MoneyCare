package com.example.moneycare.data.repository;

import com.example.moneycare.data.model.User;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRepository {
    FirebaseFirestore db;
    FirebaseUser fbUser;

    public LoginRepository(){
        db = FirebaseFirestore.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
    }
    public void createNewUser(User dbUser, Wallet wallet, FirestoreObjectCallback<String> callback){
        String userId = fbUser.getUid();

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
    public void fetchCurrentUserData(FirestoreObjectCallback<User> callback){
        String userId = fbUser.getUid();

        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                User user = User.fromMap(snapshot.getData());
                callback.onCallback(user);
            }
        });
    }
}