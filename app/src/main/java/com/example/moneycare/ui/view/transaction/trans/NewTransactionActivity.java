package com.example.moneycare.ui.view.transaction.trans;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.databinding.ActivityNewTransactionBinding;
import com.example.moneycare.ui.view.transaction.group.SelectGroupActivity;
import com.example.moneycare.ui.viewmodel.transaction.NewTransactionViewModel;
import com.example.moneycare.ui.viewmodel.transaction.WalletArrayAdapter;
import com.example.moneycare.utils.ToastUtil;
import com.example.moneycare.utils.ValidationUtil;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.List;

public class NewTransactionActivity extends AppCompatActivity {
    private NewTransactionViewModel newTransViewModel;
    private ActivityNewTransactionBinding binding;
    ActivityResultLauncher<Intent> toSelectGroupActivityLauncher = registerForActivityResult(
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
    ActivityResultLauncher<Intent> toSelectEventActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Event event = data.getParcelableExtra("event");
                        newTransViewModel.setEvent(event);
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
        initSelectGroup();
        initSelectEvent();
        initWalletList();
        // Inflate the layout for this fragment
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle(R.string.title_new_trans);
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
                        .setTitleText("Ch???n ng??y giao d???ch")
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
                boolean check = checkAllFields();
                if(check){
                    newTransViewModel.saveNewTransaction(data -> {
                        NewTransactionActivity.this.setResult(RESULT_OK);
                        NewTransactionActivity.this.finish();
                        ToastUtil.showToast(NewTransactionActivity.this, "Th??m giao d???ch th??nh c??ng");
                    }, data->{
                        ToastUtil.showToast(NewTransactionActivity.this, "L???i! Th??m giao d???ch th???t b???i");
                    });
                }
            }
        });
    }
    private boolean checkAllFields(){
        return ValidationUtil.checkEmpty(binding.newTransMoney) &&
                ValidationUtil.checkEmpty(binding.newTransGroup);
    }
    private void initSelectGroup(){
        binding.newTransGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewTransactionActivity.this, SelectGroupActivity.class);
                toSelectGroupActivityLauncher.launch(intent);
            }
        });
    }
    private void initSelectEvent(){
        binding.newTransEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewTransactionActivity.this, SelectEventActivity.class);
                toSelectEventActivityLauncher.launch(intent);
            }
        });
    }
    private void initWalletList(){
        binding.newTransWalletsSelector.setText("abc");
        newTransViewModel.setWalletList(wallets->{
            initDefaultSelectedWallet(wallets);
            initSelectWalletEvent(wallets);
        });
    }
    private void initDefaultSelectedWallet(List<Wallet> wallets){
        Wallet currentWallet = findCurrentWallet(wallets);
        if(currentWallet != null){
            binding.newTransWalletsSelector.setText(currentWallet.name);
            newTransViewModel.setWalletId(currentWallet.id);
        }
    }
    private Wallet findCurrentWallet(List<Wallet> wallets){
        String currentWalletId = getWalletFromPreference();
        for(Wallet wallet:wallets){
            if(wallet.id.equals(currentWalletId)) return wallet;
        }
        return null;
    }
    private String getWalletFromPreference(){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.transaction_preference), Context.MODE_PRIVATE);
        String walletId = sharedPref.getString(getString(R.string.pref_key_current_wallet), "");
        return walletId;
    }

    private void initSelectWalletEvent(List<Wallet> wallets){
        WalletArrayAdapter adapter = new WalletArrayAdapter(this, R.layout.dropdown_item, wallets);
        binding.newTransWalletsSelector.setAdapter(adapter);
        binding.newTransWalletsSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Wallet wallet = (Wallet) adapterView.getItemAtPosition(i);
                newTransViewModel.setWalletId(wallet.id);
            }
        });
    }

}