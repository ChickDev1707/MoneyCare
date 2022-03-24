package com.example.moneycare.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TestingViewModel extends ViewModel {
    private MutableLiveData<Object> value;
    private int _value;

    public LiveData<Object> getValue() {
        if (value == null) {
            value = new MutableLiveData<Object>();
            loadValue();
        }
        return value;
    }

    private void loadValue() {
        _value = 0;
        value.setValue(_value);
    }
    public void updateValue(){
        _value++;
        value.setValue(_value);
    }
}