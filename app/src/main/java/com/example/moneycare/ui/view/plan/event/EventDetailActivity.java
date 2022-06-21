package com.example.moneycare.ui.view.plan.event;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.data.repository.EventRepository;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.databinding.ActivityEventDetailBinding;
import com.example.moneycare.ui.view.account.MoneyFormatDialogFragment;
import com.example.moneycare.ui.view.transaction.group.UpdateGroupActivity;
import com.example.moneycare.ui.viewmodel.plan.EventViewModel;
import com.example.moneycare.utils.DateTimeUtil;
import com.example.moneycare.utils.FirestoreUtil;
import com.example.moneycare.utils.ImageLoader;
import com.example.moneycare.utils.ToastUtil;

public class EventDetailActivity extends AppCompatActivity {

    ActivityEventDetailBinding binding;
    EventViewModel eventVM;
    EventRepository repository;
    WalletRepository walletRepository;
    Event eventSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new EventRepository();
        walletRepository = new WalletRepository();
        binding = ActivityEventDetailBinding.inflate(getLayoutInflater());
        eventVM = new ViewModelProvider(this).get(EventViewModel.class);
        binding.setEventVM(eventVM);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        eventSelected = intent.getParcelableExtra("eventSelected");

        initToolbar();
        loadData();
        initButtonChangeStatus();
        initButtonWatchTransactions();
    }

    private void loadData(){
        repository.fetchEventById(eventSelected.id, event->{
            eventVM.eventName.setValue(event.name);
            eventVM.endDate.setValue(event.endDate);
            binding.daysLeftEventDetail.setText("Còn " + DateTimeUtil.daysLeft(event.endDate) + " ngày");

            if(!event.wallet.isEmpty()){
                TransactionRepository transactionRepository = new TransactionRepository();
                String walletId = FirestoreUtil.getReferenceFromPath(event.wallet).getId();
                walletRepository.fetchWallet(walletId, wallet -> {
                    eventVM.wallet.setValue(wallet);
                });
            }
            else{
                Wallet wallet = new Wallet();
                wallet.name = "Tất cả các ví";
                eventVM.wallet.setValue(wallet);
            }

            ImageLoader imageLoader = new ImageLoader(binding.imgItemEventDetail);
            imageLoader.execute(event.image);
        });
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.update_app_bar);
        toolbar.setTitle("Chi tiết sự kiện");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDetailActivity.this.finish();
            }
        });
    }

    private void initButtonChangeStatus(){
        String str = eventSelected.status.equals("end") ? "Đánh dấu chưa hoàn tất" : "Đánh dấu hoàn tất";
        binding.btnSwitchStatus.setText(str);
        if(DateTimeUtil.daysLeft(eventSelected.endDate) == 0){
            binding.btnSwitchStatus.setBackgroundColor(Color.parseColor("#DABEBE"));
        }
        else {
            binding.btnSwitchStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    repository.changeStatus(eventSelected.id,
                            eventSelected.status.equals("ongoing") ? "end" : "ongoing");
                    EventDetailActivity.this.finish();
                }
            });
        }
    }
    private void initButtonWatchTransactions(){
        binding.btnWatchTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailActivity.this, EventTransactionActivity.class );
                intent.putExtra("idEvent", eventSelected.id);
                intent.putExtra("eventName", eventSelected.name);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.update_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_item:
                Intent intent = new Intent(EventDetailActivity.this, UpdateEventActivity.class);
                intent.putExtra("event", eventSelected);
                Wallet wallet = eventVM.wallet.getValue();
                intent.putExtra("walletName",wallet.name);
                startActivity(intent);
                return true;
            case R.id.delete_item:
                showDeleteDialog();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDeleteDialog(){
        DeleteEventDialogFragment fragment = new DeleteEventDialogFragment(this, eventSelected);
        fragment.showDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}