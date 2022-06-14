package com.example.moneycare.ui.viewmodel.plan;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.data.repository.EventRepository;

import java.time.LocalDate;
import java.util.Date;

public class EventViewModel extends ViewModel {

    public MutableLiveData<Bitmap> image;
    public MutableLiveData<String> imgUrl;
    public MutableLiveData<String> eventName;
    public MutableLiveData<Date> endDate;
    public MutableLiveData<Wallet> wallet;
    public EventRepository repository;

    public MutableLiveData<Event> eventSelected;


    public EventViewModel() {
        init();
    }

    public void init (){
        repository = new EventRepository();
        imgUrl = new MutableLiveData<>("");
        eventName = new MutableLiveData<>("");
        endDate = new MutableLiveData<Date>(null);
        Wallet walletDefault = new Wallet();
        walletDefault.name = "Tất cả các ví";
        wallet = new MutableLiveData<Wallet>(walletDefault);
        image = new MutableLiveData<>();

        eventSelected = new MutableLiveData<Event>(null);
    }

    public void setImage(Uri imagePath, Bitmap bitmap){
        imgUrl.setValue(imagePath.getPath());
        image.setValue(bitmap);
    }

    public void addEvent(){
        Event newEvent = new Event(eventName.getValue(), endDate.getValue(),
                imgUrl.getValue(),wallet.getValue().id, "ongoing" );
        repository.insertEvent(newEvent);
    }
}
