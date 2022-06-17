package com.example.moneycare.ui.viewmodel.plan;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.BudgetRepository;
import com.example.moneycare.data.repository.TransactionGroupRepository;
import com.example.moneycare.data.repository.TransactionRepository;

import java.time.LocalDate;

public class NewBudgetViewModel extends ViewModel {
    public MutableLiveData<Group> groupSelected  = new MutableLiveData<Group>();
    public MutableLiveData<Long> moneyLimit  = new MutableLiveData<Long>();
    public MutableLiveData<Integer> currMonth  = new MutableLiveData<Integer>();


    public NewBudgetViewModel() {
        init();
    }

    public void init (){
        groupSelected.setValue(null);
        moneyLimit.setValue(0L);
        currMonth.setValue(LocalDate.now().getMonth().getValue());
    }
}
