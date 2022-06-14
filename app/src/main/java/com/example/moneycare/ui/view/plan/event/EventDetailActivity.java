package com.example.moneycare.ui.view.plan.event;

import static com.example.moneycare.utils.Convert.convertToNumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.databinding.ActivityAddEventBinding;
import com.example.moneycare.databinding.ActivityEventDetailBinding;
import com.example.moneycare.ui.view.plan.budget.BudgetDetailActivity;
import com.example.moneycare.ui.view.plan.budget.UpdateBudgetActivity;
import com.example.moneycare.ui.viewmodel.plan.EventViewModel;
import com.example.moneycare.utils.DateUtil;

import java.text.SimpleDateFormat;

public class EventDetailActivity extends AppCompatActivity {

    ActivityEventDetailBinding binding;
    EventViewModel eventVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDetailBinding.inflate(getLayoutInflater());
        eventVM = new ViewModelProvider(this).get(EventViewModel.class);
        binding.setEventVM(eventVM);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        initToolbar();
        loadData();
    }

    private void loadData(){
        Intent intent = getIntent();
        Event event = intent.getParcelableExtra("eventSelected");

        binding.eventNameDetail.setText(event.name);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        binding.endDateEventDetail.setText(formatter.format(event.endDate));
        binding.daysLeftEventDetail.setText("Còn " + DateUtil.daysLeft(event.endDate) + " ngày");
        if(!event.wallet.isEmpty()){
            binding.eventDetailWallet.setText(event.wallet);
        }
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.update_app_bar);
        toolbar.setTitle("Chi tiết sự kiện");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EventDetailActivity.this.setResult(Activity.RESULT_OK, intent);
                EventDetailActivity.this.finish();
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
//                Intent intent = new Intent(EventDetailActivity.this, UpdateBudgetActivity.class);
//                intent.putExtra("idBudget", idBudget);
//                intent.putExtra("imgGroup", imgGroup);
//                intent.putExtra("groupName", groupName);
//                intent.putExtra("money", convertToNumber(budgetsVM.limitOfMonth.getValue()));
//                toUpdateBudgetActivity.launch(intent);
//                return true;
            case R.id.delete_item:
//                budgetRepository.deleteBudget(idBudget);
//                BudgetDetailActivity.this.setResult(Activity.RESULT_OK);
//                BudgetDetailActivity.this.finish();
//                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}