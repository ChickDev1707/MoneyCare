package com.example.moneycare.ui.view.transaction.wallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.moneycare.R;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.ActivitySelectWalletBinding;

public class SelectWalletActivity extends AppCompatActivity {

    ActivitySelectWalletBinding binding;
    TransactionRepository transactionRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        transactionRepository = new TransactionRepository();
        transactionRepository.fetchWallets(wallets-> binding.walletList.setAdapter(new SelectWalletRvAdapter(this, wallets)));
    }

}