package com.example.moneycare.ui.viewmodel.transaction;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;

import java.util.Date;

public class NewTransactionViewModel extends ViewModel {
    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;

    public MutableLiveData<Long> money;
    public MutableLiveData<Group> group;
    public MutableLiveData<Date> date;
    public MutableLiveData<String> note;
    public MutableLiveData<String> walletId;

    public NewTransactionViewModel() {
        this.transactionRepository = new TransactionRepository();
        this.walletRepository = new WalletRepository();
        initValues();
    }
    public void initValues(){
        money = new MutableLiveData<>(0L);
        date = new MutableLiveData<>(new Date());
        note = new MutableLiveData<>("");
        group = new MutableLiveData<>();
        walletId = new MutableLiveData<>();
    }

    public void setDate(Object selection){
        Date pickedDate = new Date((Long) selection);
        date.setValue(pickedDate);
    }
    public void setGroup(Group selectedGroup){
        group.setValue(selectedGroup);
    }
    public void saveNewTransaction(){
        this.transactionRepository.saveNewTransaction(money.getValue(), group.getValue(), note.getValue(), date.getValue());
        cleanUpFields();
    }
    public void setWalletId(String id){
        walletId.setValue(id);
    }
    public void setWalletList(FirestoreListCallback<Wallet> callback){
        this.walletRepository.fetchWallets(callback);
    }
    private void cleanUpFields(){
        money.setValue(0L);
        date.setValue(new Date());
        group.setValue(new Group());
        note.setValue("");
    }
}
