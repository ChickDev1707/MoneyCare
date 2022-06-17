package com.example.moneycare.ui.viewmodel.transaction;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.utils.ImageUtil;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;

public class UpdateWalletViewModel extends ViewModel {
    private final WalletRepository walletRepository;

    public MutableLiveData<Wallet> wallet;
    public MutableLiveData<String> imgUrl;
    public MutableLiveData<Bitmap> image;
    public MutableLiveData<Boolean> updateMode;

    public UpdateWalletViewModel() {
        this.walletRepository = new WalletRepository();
        init();
    }
    public void init(){
        wallet = new MutableLiveData<>();
        imgUrl = new MutableLiveData<>();
        updateMode = new MutableLiveData<>(false);
        image = new MutableLiveData<>();
    }
    public void initWallet(Wallet wallet){
        this.wallet.setValue(wallet);
    }

    public void setImage(Uri imagePath, Bitmap bitmap){
        image.setValue(bitmap);
        imgUrl.setValue(imagePath.getPath());
    }
    public void switchUpdateMode(){
        updateMode.setValue(!updateMode.getValue());
    }
    public void updateWallet(FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        String imageBase64 = ImageUtil.toBase64(image.getValue());
        Wallet newWallet = wallet.getValue();
        newWallet.image = imageBase64;

        this.walletRepository.updateWallet(newWallet, successCallback, failureCallback);
    }
    public void deleteWallet(FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        this.walletRepository.deleteWallet(wallet.getValue(), successCallback, failureCallback);
    }
}
