package com.example.moneycare.ui.viewmodel.transaction;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.utils.ImageUtil;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;

public class NewWalletViewModel extends ViewModel {
    private WalletRepository walletRepository;

    public MutableLiveData<String> name;
    public MutableLiveData<Long> money;
    public MutableLiveData<Bitmap> image;
    public MutableLiveData<String> imgUrl;

    public NewWalletViewModel() {
        this.walletRepository = new WalletRepository();
        initValues();
    }
    public void initValues(){
        name = new MutableLiveData<>("");
        money = new MutableLiveData<>(0L);
        imgUrl = new MutableLiveData<>("");
        image = new MutableLiveData<>();
    }
    public void setImage(Uri imagePath, Bitmap bitmap){
        imgUrl.setValue(imagePath.getPath());
        image.setValue(bitmap);
    }

    public void saveNewWallet(FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        String imageBase64 = ImageUtil.toBase64(image.getValue());
        this.walletRepository.saveNewWallet(name.getValue(), money.getValue(), imageBase64, successCallback, failureCallback);
    }
}
