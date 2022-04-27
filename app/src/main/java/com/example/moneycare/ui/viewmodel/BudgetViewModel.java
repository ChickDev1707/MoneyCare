package com.example.moneycare.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.TransactionGroup;
import com.example.moneycare.data.repository.BudgetRepository;
import com.example.moneycare.data.repository.TransactionGroupRepository;

import java.time.LocalDate;
import java.util.List;

public class BudgetViewModel extends ViewModel {

    // TODO: Implement the ViewModel

    private TransactionGroupRepository transGroupRepository;
    private BudgetRepository budgetRepository;

    public MutableLiveData<String> name = new MutableLiveData<String>();
    public MutableLiveData<Long> totalBudget = new MutableLiveData<Long>();
    public MutableLiveData<Long> totalSpent = new MutableLiveData<Long>();
    public MutableLiveData<Integer> daysLeft = new MutableLiveData<Integer>();

    MutableLiveData<List<TransactionGroup>> liveTransactionGroups;

    public BudgetViewModel() {
        this.transGroupRepository = new TransactionGroupRepository();
        this.budgetRepository = new BudgetRepository();
        init();
    }
    public void init(){
        System.out.println("init");
        name.setValue("Hello");
        totalBudget.setValue(0L);
        totalSpent.setValue(0L);
        daysLeft.setValue(0);
    }

    public void fetchTransactionGroups(TransactionGroupRepository.FirestoreCallback firestoreCallback){
         transGroupRepository.fetchTransactionGroups(firestoreCallback);
    }
    public void fetchInMonth(BudgetRepository.FirestoreCallback firestoreCallback){
        budgetRepository.fetchBudgetsInMonth(firestoreCallback);
    }

//    public void calculateTotalBudget(){
////        budgetRepository.fetchAllBudget();
//    }

}