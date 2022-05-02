package com.example.moneycare.ui.viewmodel.transaction;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.utils.ImageUtil;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;

import java.util.Date;

public class NewGroupViewModel extends ViewModel {
    private TransactionRepository transactionRepository;

    public MutableLiveData<String> name;
    public MutableLiveData<Boolean> type;
    public MutableLiveData<Bitmap> image;
    public MutableLiveData<String> imgUrl;

    public NewGroupViewModel() {
        this.transactionRepository = new TransactionRepository();
        initValues();
    }
    public void initValues(){
        name = new MutableLiveData<>("");
        type = new MutableLiveData<>(true);
        imgUrl = new MutableLiveData<>("");
        image = new MutableLiveData<>();
    }
    public void setImage(Uri imagePath, Bitmap bitmap){
        imgUrl.setValue(imagePath.getPath());
        image.setValue(bitmap);
    }
    public void setGroupType(boolean value){
        type.setValue(value);
    }
    public void saveNewGroup(){
        String imageBase64 = ImageUtil.toBase64(image.getValue());
        this.transactionRepository.saveNewGroup(name.getValue(), type.getValue(), imageBase64);
    }
}
