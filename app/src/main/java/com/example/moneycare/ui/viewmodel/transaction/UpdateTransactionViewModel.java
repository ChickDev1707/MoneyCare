package com.example.moneycare.ui.viewmodel.transaction;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.User;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.data.repository.GroupRepository;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;

import java.util.Date;

public class UpdateTransactionViewModel extends ViewModel {
    private TransactionRepository transactionRepository;
    private GroupRepository groupRepository;
    private WalletRepository walletRepository;

    public MutableLiveData<Group> group;
    public MutableLiveData<UserTransaction> transaction;
    public MutableLiveData<String> wallet;
    public MutableLiveData<Boolean> updateMode;

    public UpdateTransactionViewModel() {
        this.transactionRepository = new TransactionRepository();
        this.groupRepository = new GroupRepository();
        this.walletRepository = new WalletRepository();
        init();
    }
    private void init(){
        transaction = new MutableLiveData<>();
        group = new MutableLiveData<>();
        wallet = new MutableLiveData<>();
        updateMode = new MutableLiveData<>(false);
    }
    public void initTransaction(UserTransaction transaction){
        this.transaction.setValue(transaction);
        this.groupRepository.fetchGroup(transaction.group, groupData-> group.setValue((Group) groupData));
        this.walletRepository.fetchWallet(getWalletIdFromPath(transaction.wallet), walletData -> wallet.setValue(walletData.name));
    }
    private String getWalletIdFromPath(String walletPath){
        String[] params = walletPath.split("/");
        return params[params.length - 1];
        // wallet id is the last segment in wallet path
    }
    public void switchUpdateMode(){
        updateMode.setValue(!updateMode.getValue());
    }
    public void updateTransaction(FirestoreObjectCallback callback){
        this.transactionRepository.updateTransaction(transaction.getValue(), group.getValue(), callback);
    }
    public void setGroup(Group selectedGroup){
        group.setValue(selectedGroup);
    }
    public void deleteTransaction(FirestoreObjectCallback<Void> callback){
        this.transactionRepository.deleteTransaction(transaction.getValue(), callback);
    }
}
