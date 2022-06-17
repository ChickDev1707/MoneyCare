package com.example.moneycare.ui.view.plan.event;

import static com.example.moneycare.utils.Convert.convertToNumber;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
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
import com.example.moneycare.databinding.ActivityAddEventBinding;
import com.example.moneycare.databinding.ActivityEventDetailBinding;
import com.example.moneycare.ui.view.plan.budget.BudgetDetailActivity;
import com.example.moneycare.ui.view.plan.budget.UpdateBudgetActivity;
import com.example.moneycare.ui.viewmodel.plan.EventViewModel;
import com.example.moneycare.utils.DateUtil;
import com.example.moneycare.utils.LoadImage;

import java.text.SimpleDateFormat;

public class EventDetailActivity extends AppCompatActivity {

    ActivityEventDetailBinding binding;
    EventViewModel eventVM;
    EventRepository repository;
    Event eventSelected;
    Boolean isUpdated = false;

    ActivityResultLauncher<Intent> toUpdateEventActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Event eventUpdated = data.getParcelableExtra("eventUpdated");
                        if(eventUpdated != null){
                            eventVM.eventSelected.setValue(eventUpdated);
                            eventSelected = eventUpdated;
                            loadData();
                            isUpdated = true;
                        }
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new EventRepository();
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
        eventVM.eventName.setValue(eventSelected.name);
        eventVM.endDate.setValue(eventSelected.endDate);
        binding.daysLeftEventDetail.setText("Còn " + DateUtil.daysLeft(eventSelected.endDate) + " ngày");

        if(!eventSelected.wallet.isEmpty()){
            TransactionRepository transactionRepository = new TransactionRepository();
            transactionRepository.fetchWallet(eventSelected.wallet,wallet -> {
                eventVM.wallet.setValue(wallet);
            });
        }
        else{
            Wallet wallet = new Wallet();
            wallet.name = "Tất cả các ví";
            eventVM.wallet.setValue(wallet);
        }

        LoadImage loadImage = new LoadImage(binding.imgItemEventDetail);
        loadImage.execute(eventSelected.image);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.update_app_bar);
        toolbar.setTitle("Chi tiết sự kiện");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isUpdated", isUpdated);
                EventDetailActivity.this.setResult(Activity.RESULT_OK, intent);
                EventDetailActivity.this.finish();
            }
        });
    }

    private void initButtonChangeStatus(){
        String str = eventSelected.status.equals("end") ? "Đánh dấu chưa hoàn tất" : "Đánh dấu hoàn tất";
        binding.btnSwitchStatus.setText(str);
        if(DateUtil.daysLeft(eventSelected.endDate) == 0){
            binding.btnSwitchStatus.setBackgroundColor(Color.parseColor("#DABEBE"));
        }
        else {
            binding.btnSwitchStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    repository.changeStatus(eventSelected.id,
                            eventSelected.status.equals("ongoing") ? "end" : "ongoing");
                    Intent intent = new Intent();
                    intent.putExtra("isUpdated", true);
                    EventDetailActivity.this.setResult(Activity.RESULT_OK, intent);
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
                toUpdateEventActivity.launch(intent);
                return true;
            case R.id.delete_item:
                repository.deleteEvent(eventSelected.id);
                Intent intent1 = new Intent();
                intent1.putExtra("isUpdated", true);
                EventDetailActivity.this.setResult(Activity.RESULT_OK, intent1);
                EventDetailActivity.this.finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}