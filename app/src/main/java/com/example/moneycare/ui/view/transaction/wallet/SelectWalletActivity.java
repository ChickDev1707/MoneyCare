package com.example.moneycare.ui.view.transaction.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.moneycare.R;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.databinding.ActivitySelectWalletBinding;

public class SelectWalletActivity extends AppCompatActivity {

    ActivitySelectWalletBinding binding;
    WalletRepository walletRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        walletRepository = new WalletRepository();
        walletRepository.fetchWallets(wallets-> binding.walletList.setAdapter(new SelectWalletRvAdapter(this, wallets)));
    }

}