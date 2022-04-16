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
    public int getNoTransaction(){
        return transactionList.size();
    }
    public Long getTotalMoney(){
        return 0L;
    }
}
