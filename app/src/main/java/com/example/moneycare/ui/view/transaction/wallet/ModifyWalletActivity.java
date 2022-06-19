package com.example.moneycare.ui.view.transaction.wallet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.databinding.ActivityModifyWalletBinding;
import com.example.moneycare.utils.ImageLoader;
import com.example.moneycare.utils.ToastUtil;

public class ModifyWalletActivity extends AppCompatActivity {

    ActivityModifyWalletBinding binding;
    WalletRepository repository;
    Wallet selectedWallet;
    ActivityResultLauncher<Intent> selectWalletLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        String walletId = (String) result.getData().getExtras().get("walletId");
                        setWalletData(walletId);
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyWalletBinding.inflate(getLayoutInflater());
        repository = new WalletRepository();
        setContentView(binding.getRoot());

        initToolbar();
        initWalletData();
        initWalletItemClickEvent();
        initSaveModifyWallet();
    }
    private void initWalletData(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.transaction_preference), Context.MODE_PRIVATE);
        String walletId = sharedPref.getString(getString(R.string.pref_key_current_wallet), "");
        setWalletData(walletId);
    }
    private void setWalletData(String walletId){
        repository.fetchWallet(walletId, wallet->{
            binding.walletName.setText(wallet.name);
            binding.walletMoney.setText(Long.toString(wallet.money));
            ImageLoader imageLoader = new ImageLoader(binding.walletImage);
            imageLoader.execute(wallet.image);
            selectedWallet = wallet;
        });
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle(R.string.title_new_wallet);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
    }
    private void initWalletItemClickEvent(){
        binding.walletItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyWalletActivity.this, SelectWalletActivity.class);
                selectWalletLauncher.launch(intent);
            }
        });
    }
    private void initSaveModifyWallet(){
        binding.saveModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedWallet.money = Long.parseLong(binding.walletMoney.getText().toString());
                repository.updateWallet(selectedWallet, data->{
                    ModifyWalletActivity.this.setResult(Activity.RESULT_OK);
                    ModifyWalletActivity.this.finish();
                    ToastUtil.showToast(ModifyWalletActivity.this, "Điều chỉnh số dư thành công");
                }, data -> {
                    ToastUtil.showToast(ModifyWalletActivity.this, "Lỗi! Điều chỉnh số dư thất bại");
                });
            }
        });
    }
}