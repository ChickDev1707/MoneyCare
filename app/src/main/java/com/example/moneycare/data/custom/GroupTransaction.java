package com.example.moneycare.data.custom;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;

import java.util.List;

public class GroupTransaction {
    public Group group;
    public List<UserTransaction> transactionList;

    public GroupTransaction(){}
    public GroupTransaction(Group group, List<UserTransaction> transactions){
        this.group = group;
        this.transactionList = transactions;
    }
    public long getTotalMoney(){
        long total = 0L;
        for(UserTransaction transaction:transactionList){
            total+= transaction.money;
        }
        return total;
    }
}
