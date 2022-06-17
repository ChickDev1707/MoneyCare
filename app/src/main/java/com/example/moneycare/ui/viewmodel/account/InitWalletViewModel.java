package com.example.moneycare.ui.viewmodel.account;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.data.repository.LoginRepository;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.utils.ImageUtil;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import org.jetbrains.annotations.NotNull;

public class InitWalletViewModel extends ViewModel {

    LoginRepository loginRepository;
    public MutableLiveData<String> name;
    public MutableLiveData<Long> money;
    public MutableLiveData<Bitmap> image;

    public InitWalletViewModel() {
        this.loginRepository = new LoginRepository();
        initValues();
    }
    public void initValues(){
        name = new MutableLiveData<>("");
        money = new MutableLiveData<>(0L);
        image = new MutableLiveData<>();
    }
    public void setImage(Bitmap bitmap){
        image.setValue(bitmap);
    }
    public void createUser(FirebaseUser user, FirestoreObjectCallback<String> callback){
        String imageUrl = ImageUtil.toBase64(image.getValue());
        Wallet wallet = new Wallet(null, name.getValue(), money.getValue(), imageUrl);
        this.loginRepository.createNewUser(user, wallet, callback);
    }
}
