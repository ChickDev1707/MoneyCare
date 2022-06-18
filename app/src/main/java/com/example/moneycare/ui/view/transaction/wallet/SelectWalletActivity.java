package com.example.moneycare.ui.view.transaction.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.databinding.ActivitySelectWalletBinding;
import com.example.moneycare.ui.view.transaction.trans.NewTransactionActivity;
import com.example.moneycare.ui.view.transaction.trans.SelectEventActivity;

public class SelectWalletActivity extends AppCompatActivity {

    ActivitySelectWalletBinding binding;
    WalletRepository walletRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadWalletList();
        initToolbar();
    }
    private void loadWalletList(){
        walletRepository = new WalletRepository();
        walletRepository.fetchWallets(wallets-> binding.walletList.setAdapter(new SelectWalletRvAdapter(this, wallets)));
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle("Chọn ví");
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                SelectWalletActivity.this.finish();
                onBackPressed();
            }
        });
    }
}