package com.example.moneycare.ui.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.TransactionGroup;
import com.example.moneycare.data.repository.BudgetRepository;
import com.example.moneycare.data.repository.TransactionGroupRepository;
import com.google.type.DateTime;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class BudgetViewModel extends ViewModel {

    // TODO: Implement the ViewModel

    private TransactionGroupRepository transGroupRepository;
    private BudgetRepository budgetRepository;

    //fragment budget
    public MutableLiveData<String> name = new MutableLiveData<String>();
    public MutableLiveData<Long> totalBudget = new MutableLiveData<Long>();
    public MutableLiveData<Long> totalSpent = new MutableLiveData<Long>();
    public MutableLiveData<Integer> daysLeft = new MutableLiveData<Integer>();

    //fragment add budget
    public MutableLiveData<TransactionGroup> groupSelected  = new MutableLiveData<TransactionGroup>();
    public MutableLiveData<Long> moneyLimit  = new MutableLiveData<Long>();

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
        moneyLimit.setValue(0L);
    }

    public void fetchTransactionGroupsBYBudget(BudgetRepository.FirestoreCallback firestoreCallback){
         budgetRepository.fetchGroupByBudgetInMonth(firestoreCallback);
    }
    public void fetchInMonth(BudgetRepository.FirestoreCallback firestoreCallback){
        budgetRepository.fetchBudgetsInMonth(firestoreCallback);
    }

    public void goToFragmentAddBudget(View view){
        Navigation.findNavController(view).navigate(R.id.action_budgetFragment_to_addBudgetFragment);
    }
    public void addBudget(View view){
        budgetRepository.insertBudget(new Budget("transaction-groups/" + groupSelected.getValue().getId(),Long.parseLong(moneyLimit.getValue().toString())
                ,Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())));
        Navigation.findNavController(view).popBackStack();
    }

}