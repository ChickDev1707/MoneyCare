package com.example.moneycare.ui.view.plan.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.moneycare.R;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.ActivityEventTransactionBinding;
import com.example.moneycare.ui.view.transaction.trans.GroupTransactionRecyclerViewAdapter;
import com.example.moneycare.ui.viewmodel.plan.EventViewModel;
import com.example.moneycare.utils.appinterface.ReloadTransactionActivity;

public class EventTransactionActivity extends ReloadTransactionActivity {

    ActivityEventTransactionBinding binding;
    TransactionRepository transactionRepository;
    EventViewModel eventVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventTransactionBinding.inflate(getLayoutInflater());
        eventVM = new ViewModelProvider(this).get(EventViewModel.class);
        binding.setEventVM(eventVM);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        init();
        initToolbar();
        reloadTransactionList();
    }

    private void init(){
        transactionRepository = new TransactionRepository();
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle(getIntent().getStringExtra("eventName"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventTransactionActivity.this.finish();
                EventTransactionActivity.this.onBackPressed();
            }
        });
    }
    @Override
    public void reloadTransactionList(){
        //load list
        LinearLayout layoutContainer = findViewById(R.id.event_trans_container);
        LinearLayout layoutEmpty = findViewById(R.id.event_trans_empty);
        LinearLayout layoutLoading = findViewById(R.id.loading);

        String idEvent = getIntent().getStringExtra("idEvent");
        transactionRepository.getTransactionsByEvent(idEvent, groupTransactions -> {
            layoutLoading.setVisibility(View.INVISIBLE);
            RecyclerView transList = binding.eventTransactionList;
            if(groupTransactions.size() == 0){
                layoutEmpty.setVisibility(View.VISIBLE);
                layoutContainer.setVisibility(View.INVISIBLE);
            }
            else {
                transList.setAdapter(new GroupTransactionRecyclerViewAdapter(groupTransactions));
                eventVM.initMoneyInAndOut(groupTransactions);
                layoutEmpty.setVisibility(View.INVISIBLE);
                layoutContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadTransactionList();
    }
}
