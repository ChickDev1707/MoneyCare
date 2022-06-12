package com.example.moneycare.ui.viewmodel.plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.BudgetRepository;
import com.example.moneycare.data.repository.TransactionGroupRepository;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.utils.Convert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BudgetViewModel extends ViewModel {

    // TODO: Implement the ViewModel

    private TransactionGroupRepository transGroupRepository;
    private BudgetRepository budgetRepository;
    private TransactionRepository transactionRepository;

    public List<Group> activeGroups;

    //fragment budget
    public MutableLiveData<String> name = new MutableLiveData<String>();
    public MutableLiveData<String> totalBudget = new MutableLiveData<String>();
    public Long totalBudgetImpl;
    public MutableLiveData<String> totalSpent = new MutableLiveData<String>();
    public Long totalSpentImpl;
    public MutableLiveData<Integer> daysLeft = new MutableLiveData<Integer>();
    public MutableLiveData<String> spendableMoney = new MutableLiveData<String>();
    public Long spendableMoneyImpl;

    //fragment detail budget
    public MutableLiveData<String> totalSpentByGroup  = new MutableLiveData<String>(); // Tổng đã chi
    public MutableLiveData<String> limitOfMonth  = new MutableLiveData<String>(); // Giới hạn
    public MutableLiveData<String> spendPerDay  = new MutableLiveData<String>(); // Nên chi hàng ngày


    public MutableLiveData<Group> groupSelected  = new MutableLiveData<Group>();
    public MutableLiveData<Long> moneyLimit  = new MutableLiveData<Long>();
    public MutableLiveData<Integer> currMonth  = new MutableLiveData<Integer>();


    MutableLiveData<List<Group>> liveTransactionGroups;

    public BudgetViewModel() {
        this.transGroupRepository = new TransactionGroupRepository();
        this.budgetRepository = new BudgetRepository();
        this.transactionRepository = new TransactionRepository();
        init();
    }
    public void init(){
        System.out.println("init");
        name.setValue("Hello");
        totalBudget.setValue("0.0 K");
        totalSpent.setValue("0.0 K");
        spendableMoney.setValue("0.0 K");
        daysLeft.setValue(0);
        totalBudgetImpl = 0L;
        totalSpentImpl = 0L;
        spendableMoneyImpl = 0L;


        totalSpentByGroup.setValue("0");
        limitOfMonth.setValue("0");
        spendPerDay.setValue("0");

        moneyLimit.setValue(0L);
        groupSelected.setValue(null);
        currMonth.setValue(LocalDate.now().getMonth().getValue());

    }

    public void calculateSpendPerDay(){
        Long spd = 0L;
        Long remainMoney = Convert.convertToNumber(limitOfMonth.getValue()) - Convert.convertToNumber(totalSpentByGroup.getValue());

        daysLeft.setValue(LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth() + 1);

        if (daysLeft.getValue() == 1){
            spd = remainMoney;
        }
        else{
            spd = remainMoney / daysLeft.getValue();
        }
        spendPerDay.setValue(Convert.convertToThousandsSeparator(spd));
    }

    public void getTotalSpentInMonth(){
        budgetRepository.fetchBudgetsInMonth(budgets -> {
           for (Budget budget : (List<Budget>)budgets){
               transactionRepository.getTotalSpendByGroup(total -> {
                   totalSpentImpl = totalSpentImpl + (Long)total;
                   totalSpent.setValue(Convert.convertToMoneyCompact(totalSpentImpl));
                   spendableMoneyImpl = totalBudgetImpl - totalSpentImpl;
                   spendableMoney.setValue(Convert.convertToMoneyCompact(spendableMoneyImpl));
               },budget.getDate(), budget.getGroup_id());
           }
        });
    }
    public void fetchTransactionsByGroup(Date startDate, String idGroup){
        transactionRepository.getTotalSpendByGroup(total -> {
            totalSpentByGroup.setValue(Convert.convertToThousandsSeparator((Long)total));
            calculateSpendPerDay();
        }, startDate, idGroup);
    }
    public void fetchTransactionGroupsByBudget(BudgetRepository.FirestoreMultiCallback callback){
         transGroupRepository.fetchTransactionGroups(groups -> {
             budgetRepository.fetchBudgetsInMonth(budgets -> {
                 List<Group> groupList = new ArrayList<Group>();
                 ((List<Budget>)budgets).forEach(element -> {
                     Group group = ((List<Group>)groups).stream()
                             .filter(x -> x.id.equals(element.getGroup_id().split("/")[1]))
                             .findFirst()
                             .orElse(null);
                     if(group != null)
                        groupList.add(group);
                 });
                 callback.onCallback(groupList, budgets);
             });
         });
    }

}