package com.example.moneycare.ui.view.transaction.trans;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.databinding.ActivityUpdateTransactionBinding;
import com.example.moneycare.ui.view.transaction.group.SelectGroupActivity;
import com.example.moneycare.ui.viewmodel.transaction.UpdateTransactionViewModel;
import com.example.moneycare.utils.ToastUtil;
import com.example.moneycare.utils.ValidationUtil;

import org.jetbrains.annotations.NotNull;

public class UpdateTransactionActivity extends AppCompatActivity {
    private UpdateTransactionViewModel updateTransViewModel;
    private ActivityUpdateTransactionBinding binding;
    ActivityResultLauncher<Intent> toGroupActivityLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    Group group = data.getParcelableExtra("group");
                    updateTransViewModel.setGroup(group);
                }
            }
    });
    ActivityResultLauncher<Intent> toSelectEventActivityLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    Event event = data.getParcelableExtra("event");
                    updateTransViewModel.setEvent(event);
                }
            }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTransViewModel =  new ViewModelProvider(this).get(UpdateTransactionViewModel.class);
        binding = ActivityUpdateTransactionBinding.inflate(getLayoutInflater());
        binding.setUpdateTransVM(updateTransViewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        initToolbar();
        initSelectGroup();
        initSelectEvent();
        initUpdateTransactionBtn();
        UserTransaction transaction = (UserTransaction) getIntent().getParcelableExtra("transaction");
        updateTransViewModel.initTransaction(transaction);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.update_app_bar);
        toolbar.setTitle("S???a giao d???ch");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                UpdateTransactionActivity.this.finish();
                onBackPressed();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.update_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_item:
                updateTransViewModel.switchUpdateMode();
                return true;
            case R.id.delete_item:
                updateTransViewModel.deleteTransaction(data -> {
                    UpdateTransactionActivity.this.setResult(RESULT_OK);
                    UpdateTransactionActivity.this.finish();
                    ToastUtil.showToast(UpdateTransactionActivity.this, "X??a giao d???ch th??nh c??ng");
                },
                data->{
                    ToastUtil.showToast(UpdateTransactionActivity.this, "L???i! X??a giao d???ch th???t b???i");
                });
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSelectGroup(){
        binding.updateTransGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateTransactionActivity.this, SelectGroupActivity.class);
                toGroupActivityLauncher.launch(intent);
            }
        });
    }
    private void initUpdateTransactionBtn(){
        binding.updateTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = checkAllFields();
                if(check){
                    updateTransViewModel.updateTransaction(object -> {
                        UpdateTransactionActivity.this.setResult(RESULT_OK);
                        UpdateTransactionActivity.this.finish();
                        ToastUtil.showToast(UpdateTransactionActivity.this, "C???p nh???t giao d???ch th??nh c??ng");
                    },
                    object->{
                        ToastUtil.showToast(UpdateTransactionActivity.this, "L???i! C???p nh???t giao d???ch th???t b???i");
                    });
                }
            }
        });
    }
    private void initSelectEvent(){
        binding.newTransEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateTransactionActivity.this, SelectEventActivity.class);
                toSelectEventActivityLauncher.launch(intent);
            }
        });
    }
    private boolean checkAllFields(){
        return ValidationUtil.checkEmpty(binding.updateTransMoney) &&
                ValidationUtil.checkEmpty(binding.updateTransGroup);
    }
}