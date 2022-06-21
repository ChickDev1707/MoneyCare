package com.example.moneycare.ui.viewmodel.plan;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.BudgetRepository;
import com.example.moneycare.data.repository.GroupRepository;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.example.moneycare.utils.appinterface.FirestoreMultiListCallback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BudgetViewModel extends ViewModel {

    // TODO: Implement the ViewModel

    private GroupRepository groupRepository;
    private BudgetRepository budgetRepository;
    private TransactionRepository transactionRepository;

    public List<Group> activeGroups;

    //fragment budget
    public MutableLiveData<String> name = new MutableLiveData<String>();
    public MutableLiveData<Long> totalBudget = new MutableLiveData<Long>();
    public MutableLiveData<Long> totalSpent = new MutableLiveData<Long>();
    public MutableLiveData<Integer> daysLeft = new MutableLiveData<Integer>();
    public MutableLiveData<Long> spendableMoney = new MutableLiveData<Long>();

    //fragment detail budget
    public MutableLiveData<Long> totalSpentByGroup  = new MutableLiveData<Long>(); // Tổng đã chi
    public MutableLiveData<Long> limitOfMonth  = new MutableLiveData<Long>(); // Giới hạn
    public MutableLiveData<Long> spendPerDay  = new MutableLiveData<Long>(); // Nên chi hàng ngày


    public MutableLiveData<Group> groupSelected  = new MutableLiveData<Group>();
    public MutableLiveData<Long> moneyLimit  = new MutableLiveData<Long>();
    public MutableLiveData<Integer> currMonth  = new MutableLiveData<Integer>();


    MutableLiveData<List<Group>> liveTransactionGroups;

    public BudgetViewModel() {
        this.groupRepository = new GroupRepository();
        this.budgetRepository = new BudgetRepository();
        this.transactionRepository = new TransactionRepository();
        init();
    }
    public void init(){
        System.out.println("init");
        name.setValue("Hello");
        totalBudget.setValue(0L);
        totalSpent.setValue(0L);
        spendableMoney.setValue(0L);
        daysLeft.setValue(0);


        totalSpentByGroup.setValue(0L);
        limitOfMonth.setValue(0L);
        spendPerDay.setValue(0L);

        moneyLimit.setValue(0L);
        groupSelected.setValue(null);
        currMonth.setValue(LocalDate.now().getMonth().getValue());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calculateSpendPerDay(){
        Long spd = 0L;
        Long remainMoney = limitOfMonth.getValue() - totalSpentByGroup.getValue();

        daysLeft.setValue(LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth() + 1);

        if (daysLeft.getValue() == 1){
            spd = remainMoney;
        }
        else{
            spd = remainMoney / daysLeft.getValue();
        }
        spendPerDay.setValue(spd);
    }

    public void getTotalSpentInMonth(){
        budgetRepository.fetchBudgetsInMonth(budgets -> {
           for (Budget budget : (List<Budget>)budgets){
               transactionRepository.getTotalSpendByGroup(total -> {
                   totalSpent.setValue(totalSpent.getValue() + (Long)total);
                   spendableMoney.setValue(totalBudget.getValue() - totalSpent.getValue());
               },budget.getDate(), budget.getGroup());
           }
        });
    }
    public void fetchTransactionsByGroup(Date startDate, String idGroup){
        transactionRepository.getTotalSpendByGroup(total -> {
            totalSpentByGroup.setValue((Long)total);
            calculateSpendPerDay();
        }, startDate, idGroup);
    }
    public void fetchTransactionGroupsByBudget(FirestoreMultiListCallback callback){
        groupRepository.fetchGroups(groups -> {
            budgetRepository.fetchBudgetsInMonth(budgets -> {
                List<Group> groupList = new ArrayList<Group>();
                ((List<Budget>)budgets).forEach(element -> {
                    String[] arr = element.getGroup().split("/");
                    Group group = ((List<Group>)groups).stream()
                            .filter(x -> x.id.equals(arr[arr.length - 1]))
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