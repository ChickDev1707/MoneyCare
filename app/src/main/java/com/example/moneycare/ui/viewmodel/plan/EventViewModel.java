package com.example.moneycare.ui.viewmodel.plan;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.repository.EventRepository;
import com.example.moneycare.utils.ImageUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class EventViewModel extends ViewModel {

    public MutableLiveData<Bitmap> image;
    public MutableLiveData<String> imgUrl;
    public MutableLiveData<String> eventName;
    public MutableLiveData<Date> endDate;
    public EventRepository repository;

    //even transactions
    public MutableLiveData<Long> moneyIn;
    public MutableLiveData<Long> moneyOut;
    public MutableLiveData<Long> moneyTotal;

    public MutableLiveData<Event> eventSelected;


    public EventViewModel() {
        init();
    }

    public void init (){
        repository = new EventRepository();
        imgUrl = new MutableLiveData<>("");
        eventName = new MutableLiveData<>("");
        endDate = new MutableLiveData<Date>(new Date());
        image = new MutableLiveData<>();

        eventSelected = new MutableLiveData<Event>(null);

        this.moneyIn = new MutableLiveData<>(0L);
        this.moneyOut = new MutableLiveData<>(0L);
        this.moneyTotal = new MutableLiveData<>(0L);
    }

    public void setImage(Uri imagePath, Bitmap bitmap){
        imgUrl.setValue(imagePath.getPath());
        image.setValue(bitmap);
    }

    public void addEvent(){
        Event newEvent = new Event(null, eventName.getValue(), endDate.getValue(),
                image.getValue() == null ? "" : ImageUtil.toBase64(image.getValue()), "ongoing" );
        repository.insertEvent(newEvent);
    }
    public void updateEvent(String idEvent){
        Event event = new Event(idEvent, eventName.getValue(), endDate.getValue(),
                image.getValue() == null?"" : ImageUtil.toBase64(image.getValue()), "ongoing" );
        repository.updateEvent(event);
    }

    public void initMoneyInAndOut(List<GroupTransaction> groupTransactionList){
        long in = 0L;
        long out = 0L;
        for(GroupTransaction groupTransaction:groupTransactionList){
            if(groupTransaction.group.type) in += groupTransaction.getTotalMoney();
            else out += groupTransaction.getTotalMoney();
        }
        moneyTotal.setValue(in - out);
        moneyIn.setValue(in);
        moneyOut.setValue(out);
    }
}
