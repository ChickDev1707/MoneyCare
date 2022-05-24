package com.example.moneycare.ui.viewmodel.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.LoginRepository;
import com.example.moneycare.data.custom.Result;
import com.example.moneycare.data.model.User;
import com.example.moneycare.R;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<String> name;
    public MutableLiveData<Long> money;


}