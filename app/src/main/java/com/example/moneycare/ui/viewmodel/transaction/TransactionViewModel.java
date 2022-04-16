package com.example.moneycare.ui.viewmodel.transaction;

import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.utils.appenum.TransactionTimeFrame;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;

import java.util.Date;

public class TransactionViewModel extends ViewModel {

    private TransactionRepository transactionRepository;


    public TransactionViewModel() {
        this.transactionRepository = new TransactionRepository();
        init();
    }
    public void init(){
        System.out.println("init");
    }

    public void fetchTransactions(TransactionTimeFrame timeFrame, Date date, FirestoreListCallback firestoreCallback){
        switch (timeFrame){
            case DAY:
                this.transactionRepository.fetchDayTransactions(date, firestoreCallback);
                break;
            case MONTH:
                this.transactionRepository.fetchMonthTransactions(date, firestoreCallback);
                break;
            case YEAR:
                this.transactionRepository.fetchYearTransactions(date, firestoreCallback);
                break;
        }
//        transactionRepository.fetchTransactions(firestoreCallback);
    }

}