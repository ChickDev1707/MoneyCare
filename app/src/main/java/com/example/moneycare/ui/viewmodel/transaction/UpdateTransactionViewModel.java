package com.example.moneycare.ui.viewmodel.transaction;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.User;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;

import java.util.Date;

public class UpdateTransactionViewModel extends ViewModel {
    private TransactionRepository transactionRepository;

    public MutableLiveData<Group> group;
    public MutableLiveData<UserTransaction> transaction;
//    private UserTransaction _transaction;
    public MutableLiveData<Boolean> updateMode;

    public UpdateTransactionViewModel() {
        this.transactionRepository = new TransactionRepository();

        transaction = new MutableLiveData<>();
        updateMode = new MutableLiveData<>(false);
    }
    public void initTransaction(UserTransaction transaction){
        this.transaction.setValue(transaction);
        this.transactionRepository.fetchGroup(transaction.group, groupData-> group = new MutableLiveData<>((Group) groupData));
    }
    public void switchUpdateMode(){
        updateMode.setValue(!updateMode.getValue());
    }
    public void updateTransaction(){
//        System.out.println(transaction.getValue().money);
        this.transactionRepository.updateTransaction(transaction.getValue(), group.getValue());
    }
    public void setGroup(Group selectedGroup){
        group.setValue(selectedGroup);
    }
}
