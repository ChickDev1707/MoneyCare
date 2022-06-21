package com.example.moneycare.ui.viewmodel.transaction;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.data.repository.EventRepository;
import com.example.moneycare.data.repository.GroupRepository;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.utils.FirestoreUtil;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;

public class UpdateTransactionViewModel extends ViewModel {
    private TransactionRepository transactionRepository;
    private GroupRepository groupRepository;
    private WalletRepository walletRepository;
    private EventRepository eventRepository;

    public MutableLiveData<Group> group;
    public MutableLiveData<Event> event;
    public MutableLiveData<UserTransaction> transaction;
    public MutableLiveData<String> wallet;
    public MutableLiveData<Boolean> updateMode;

    public UpdateTransactionViewModel() {
        initRepositories();
        initLiveData();
    }
    private void initRepositories(){
        this.transactionRepository = new TransactionRepository();
        this.groupRepository = new GroupRepository();
        this.walletRepository = new WalletRepository();
        this.eventRepository = new EventRepository();
    }
    private void initLiveData(){
        transaction = new MutableLiveData<>();
        group = new MutableLiveData<>();
        event = new MutableLiveData<>(new Event());
        wallet = new MutableLiveData<>();
        updateMode = new MutableLiveData<>(false);
    }
    public void initTransaction(UserTransaction transaction){
        this.transaction.setValue(transaction);
        this.groupRepository.fetchGroup(transaction.group, groupData-> group.setValue((Group) groupData));
        this.walletRepository.fetchWallet(getWalletIdFromPath(transaction.wallet), walletData -> wallet.setValue(walletData.name));
        if(!transaction.eventId.equals("")) this.eventRepository.fetchEventById(transaction.eventId, eventData-> event.setValue(eventData));
    }
    private String getWalletIdFromPath(String walletPath){
        return FirestoreUtil.getReferenceFromPath(walletPath).getId();
        // wallet id is the last segment in wallet path
    }
    public void switchUpdateMode(){
        updateMode.setValue(!updateMode.getValue());
    }
    public void updateTransaction(FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        Event eventValue = event.getValue();
        if(eventValue.id == null || eventValue.name.equals("")){
            // event is clear or not selected
            eventValue.id = "";
        }
        this.transactionRepository.updateTransaction(transaction.getValue(), group.getValue(), eventValue.id, successCallback, failureCallback);
    }
    public void setGroup(Group selectedGroup){
        group.setValue(selectedGroup);
    }
    public void setEvent(Event selectedEvent){
        event.setValue(selectedEvent);
    }
    public void deleteTransaction(FirestoreObjectCallback<Void> successCallback, FirestoreObjectCallback<Void> failureCallback){
        this.transactionRepository.deleteTransaction(transaction.getValue(), successCallback, failureCallback);
    }
}
