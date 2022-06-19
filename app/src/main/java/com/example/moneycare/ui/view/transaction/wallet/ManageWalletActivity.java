package com.example.moneycare.ui.view.transaction.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.repository.WalletRepository;
import com.example.moneycare.databinding.ActivityManageWalletBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageWalletActivity extends AppCompatActivity {

    ActivityManageWalletBinding binding;
    WalletRepository walletRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initToolbar();
        loadWalletList();
        initOpenAddWalletBtn();
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle("Quản lý ví");
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                ManageWalletActivity.this.finish();
                onBackPressed();
            }
        });
    }
    private void loadWalletList(){
        walletRepository = new WalletRepository();
        walletRepository.fetchWallets(wallets-> binding.walletList.setAdapter(new WalletManageRvAdapter(this, wallets)));
    }
    private void initOpenAddWalletBtn(){
        FloatingActionButton openAddWalletBtn = findViewById(R.id.floating_add_btn);
        openAddWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageWalletActivity.this, NewWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWalletList();
    }
}