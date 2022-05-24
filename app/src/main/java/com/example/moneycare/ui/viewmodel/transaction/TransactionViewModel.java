package com.example.moneycare.ui.viewmodel.transaction;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.utils.DateUtil;
import com.example.moneycare.utils.appenum.TransactionTimeFrame;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;

import java.util.Date;
import java.util.List;

public class TransactionViewModel extends ViewModel {

    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    public MutableLiveData<String> timeTitle;
    public MutableLiveData<Long> moneyIn;
    public MutableLiveData<Long> moneyOut;
    public MutableLiveData<Long> moneyTotal;

    public TransactionViewModel() {
        this.transactionRepository = new TransactionRepository();
        this.walletRepository = new WalletRepository();

        this.timeTitle = new MutableLiveData<>();
        this.moneyIn = new MutableLiveData<>(0L);
        this.moneyOut = new MutableLiveData<>(0L);
        this.moneyTotal = new MutableLiveData<>(0L);

    }
    public void setTransactionUI(TransactionTimeFrame timeFrame, Date date, FirestoreListCallback firestoreCallback){
        String title = "";
        switch (timeFrame){
            case DAY:
                this.transactionRepository.fetchDayTransactions(date, firestoreCallback);
                title = DateUtil.getDateString(date);
                break;
            case MONTH:
                this.transactionRepository.fetchMonthTransactions(date, firestoreCallback);
                title = DateUtil.getMonthString(date);
                break;
            case YEAR:
                this.transactionRepository.fetchYearTransactions(date, firestoreCallback);
                title = DateUtil.getYearString(date);
                break;
        }
        timeTitle.setValue(title);
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
    public void fetchWallet(String id, FirestoreObjectCallback<Wallet> callback){
        this.walletRepository.fetchWallet(id, callback);
    }
    public void fetchFirstWallet(FirestoreObjectCallback<Wallet> callback){
        this.walletRepository.fetchFirstWallet(callback);
    }
}