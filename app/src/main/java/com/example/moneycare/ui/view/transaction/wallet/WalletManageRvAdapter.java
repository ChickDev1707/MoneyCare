package com.example.moneycare.ui.view.transaction.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.ui.view.transaction.group.UpdateGroupActivity;

import java.util.List;

public class WalletManageRvAdapter extends WalletMainRvAdapter{
    public WalletManageRvAdapter(ManageWalletActivity activity, List<Wallet> wallets) {
        super(activity, wallets);
    }
    private void initWalletItemClickEvent(WalletMainRvAdapter.ViewHolder holder, int position){
        Wallet wallet = wallets.get(position);
        holder.walletItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UpdateWalletActivity.class);
                intent.putExtra("wallet", wallet);
                ((ManageWalletActivity) activity).getReloadWalletListLauncher().launch(intent);
            }
        });
    }

    @Override
    public void onBindViewHolder(WalletMainRvAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        initWalletItemClickEvent(holder, position);
    }
}
