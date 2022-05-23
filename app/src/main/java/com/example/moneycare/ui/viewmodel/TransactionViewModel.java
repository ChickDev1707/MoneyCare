package com.example.moneycare.ui.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Transaction;
import com.example.moneycare.data.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TransactionViewModel extends ViewModel {

    private TransactionRepository transactionRepository;

    public MutableLiveData<Long> money = new MutableLiveData<Long>();
    public MutableLiveData<Date> date = new MutableLiveData<Date>();

    MutableLiveData<List<Transaction>> liveTransactions;
//    List<Transaction> transactions;

    public TransactionViewModel() {
        this.transactionRepository = new TransactionRepository();
        init();
    }
    public void init(){

        System.out.println("init");
        money.setValue(0L);
        date.setValue(new Date());
    }

    public void fetchTransactions(TransactionRepository.FirestoreListCallback firestoreCallback){
        transactionRepository.fetchTransactions(firestoreCallback);
    }

}