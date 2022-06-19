package com.example.moneycare.ui.viewmodel.transaction;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.GroupRepository;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.utils.ImageUtil;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;

public class UpdateGroupViewModel extends ViewModel {
    private final GroupRepository groupRepository;

    public MutableLiveData<Group> group;
    public MutableLiveData<String> imgUrl;
    public MutableLiveData<Bitmap> image;
    public MutableLiveData<Boolean> updateMode;

    public UpdateGroupViewModel() {
        this.groupRepository = new GroupRepository();
        init();
    }
    public void init(){
        group = new MutableLiveData<>();
        imgUrl = new MutableLiveData<>("");
        updateMode = new MutableLiveData<>(false);
        image = new MutableLiveData<>();
    }
    public void initGroup(Group group){
        this.group.setValue(group);

        Bitmap groupImage = ImageUtil.toBitmap(group.image);
        image.setValue(groupImage);
    }

    public void setImage(Uri imagePath, Bitmap bitmap){
        image.setValue(bitmap);
        imgUrl.setValue(imagePath.getPath());
    }
    public void switchUpdateMode(){
        updateMode.setValue(!updateMode.getValue());
    }
    public void updateGroup(FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        String imageBase64 = ImageUtil.toBase64(image.getValue());
        Group newGroup = group.getValue();
        newGroup.image = imageBase64;

        this.groupRepository.updateGroup(newGroup, successCallback, failureCallback);
    }
    public void deleteGroup(FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        this.groupRepository.deleteGroup(group.getValue(), successCallback, failureCallback);
    }
}
