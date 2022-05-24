package com.example.moneycare.ui.view.transaction.wallet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.databinding.ActivityManageWalletBinding;
import com.example.moneycare.ui.view.transaction.trans.TransactionFragment;

public class ManageWalletActivity extends AppCompatActivity {

    ActivityManageWalletBinding binding;
    WalletRepository walletRepository;

    ActivityResultLauncher<Intent> reloadWalletListLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        loadWalletList();
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadWalletList();
        initOpenAddWalletBtn();
    }
    private void loadWalletList(){
        walletRepository = new WalletRepository();
        walletRepository.fetchWallets(wallets-> binding.walletList.setAdapter(new WalletManageRvAdapter(this, wallets)));
    }
    private void initOpenAddWalletBtn(){
        binding.openAddWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageWalletActivity.this, NewWalletActivity.class);
                reloadWalletListLauncher.launch(intent);
            }
        });
    }
    public ActivityResultLauncher<Intent> getReloadWalletListLauncher(){
        return reloadWalletListLauncher;
    }
}