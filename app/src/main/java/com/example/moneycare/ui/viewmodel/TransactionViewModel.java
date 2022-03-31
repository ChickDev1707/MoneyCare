package com.example.moneycare.ui.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Transaction;
import com.example.moneycare.data.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionViewModel extends ViewModel {

    private TransactionRepository transactionRepository;

//    MutableLiveData<List<Transaction>> transactions;
    List<Transaction> transactions;

    public TransactionViewModel() {
//        this.transactionRepository = transactionRepository;
        init();
    }
    public void init(){
        transactions = new ArrayList<Transaction>();
        for(int i = 0; i< 10; i++){
            Date date = new Date();
            Transaction trans = new Transaction(20000, date);
            this.transactions.add(trans);
        }
    }
    public List<Transaction> getTransactions(){
        return transactions;
    }

}