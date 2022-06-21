package com.example.moneycare.ui.viewmodel.plan;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.BudgetRepository;

import java.time.LocalDate;
import java.util.Date;

public class NewBudgetViewModel extends ViewModel {
    BudgetRepository budgetRepository;

    public MutableLiveData<Group> selectedGroup  = new MutableLiveData<Group>();
    public MutableLiveData<Long> moneyLimit  = new MutableLiveData<Long>();
    public MutableLiveData<Integer> currentMonth  = new MutableLiveData<Integer>();


    public NewBudgetViewModel() {
        init();
    }

    public void init (){
        budgetRepository = new BudgetRepository();
        selectedGroup.setValue(null);
        moneyLimit.setValue(0L);
        currentMonth.setValue(LocalDate.now().getMonth().getValue());
    }
    public void saveNewBudget(){
        Budget budget = new Budget(getGroupPath(), moneyLimit.getValue(), new Date());
        budgetRepository.insertBudget(budget);
    }
    private String getGroupPath(){
        String groupPath = "";
        Group group = selectedGroup.getValue();
        if(group.isDefault){
            groupPath = "/transaction-groups/" + group.id;
        }
        else {
            groupPath = "users/" + budgetRepository.userId + "/transaction-groups/" + group.id;
        }
        return groupPath;
    }
}
