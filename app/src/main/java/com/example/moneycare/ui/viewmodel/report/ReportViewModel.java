package com.example.moneycare.ui.viewmodel.report;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.data.repository.ReportRepository;
import com.example.moneycare.utils.DateUtil;
import com.example.moneycare.utils.appenum.TransactionTimeFrame;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;

import java.util.Date;
import java.util.List;

public class ReportViewModel extends ViewModel {

    private ReportRepository reportRepository;
    public  MutableLiveData<String> timeTitle;
    public  MutableLiveData<Long>   moneyIn;
    public  MutableLiveData<Long>   moneyOut;
    public  MutableLiveData<Long>   moneyTotal;
    public GroupTransaction listTransInMonth;

    public ReportViewModel() {
        this.reportRepository = new ReportRepository();

        this.timeTitle = new MutableLiveData<>();
        this.moneyIn = new MutableLiveData<>(0L);
        this.moneyOut = new MutableLiveData<>(0L);
        this.moneyTotal = new MutableLiveData<>(0L);


    }

    public void getListTransReport(Date date, FirestoreListCallback firestoreListCallback){
        this.reportRepository.getMonthTransactions(date, firestoreListCallback);
    }
    public void setUI(TransactionTimeFrame timeFrame, Date date, FirestoreListCallback firestoreCallback){
        String title = "";
        switch (timeFrame){
            case DAY:
                this.reportRepository.fetchMonthTransactions(date, firestoreCallback);
                title = DateUtil.getMonthString(date);
                break;
            case MONTH:
                this.reportRepository.fetchMonthTransactions(date, firestoreCallback);
                title = DateUtil.getMonthString(date);
                break;
            case YEAR:
                this.reportRepository.fetchYearTransactions(date, firestoreCallback);
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


}