package com.example.moneycare.ui.view.transaction;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.ActivityNewTransactionBinding;
import com.example.moneycare.ui.viewmodel.transaction.NewTransactionViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class NewTransactionActivity extends AppCompatActivity {
    private NewTransactionViewModel newTransViewModel;
    private ActivityNewTransactionBinding binding;
    ActivityResultLauncher<Intent> toGroupActivityLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Intent data = result.getData();
                Group group = data.getParcelableExtra("group");
                newTransViewModel.setGroup(group);
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newTransViewModel = new ViewModelProvider(this).get(NewTransactionViewModel.class);
        binding = ActivityNewTransactionBinding.inflate(getLayoutInflater());
        binding.setNewTransVM(newTransViewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        // Set up binding
        initToolbar();
        initPickDateInput();
        initSaveTransBtn();
        initSelectGroupEvent();
        // Inflate the layout for this fragment
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.new_trans_action_bar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                NewTransactionActivity.this.finish();
                onBackPressed();
            }
        });
    }
    private void initPickDateInput(){
//        binding.newTransDate.setEnabled(false);
        binding.newTransDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long today = MaterialDatePicker.todayInUtcMilliseconds();
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                MaterialDatePicker datePicker = builder
                        .setTitleText("Chọn ngày giao dịch")
                        .setSelection(today)
                        .build();
                datePicker.show(getSupportFragmentManager(), "Pick date");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        newTransViewModel.setDate(selection);
                    }
                });
            }
        });
    }
    private void initSaveTransBtn(){
        binding.saveNewTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTransViewModel.saveNewTransaction();
                NewTransactionActivity.this.setResult(RESULT_OK);
                NewTransactionActivity.this.finish();
            }
        });
    }
    private void initSelectGroupEvent(){
        binding.newTransGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewTransactionActivity.this, GroupActivity.class);
                toGroupActivityLauncher.launch(intent);
            }
        });
    }
}