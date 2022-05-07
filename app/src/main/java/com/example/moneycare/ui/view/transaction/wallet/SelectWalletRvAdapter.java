package com.example.moneycare.ui.view.transaction.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.databinding.GroupItemBinding;
import com.example.moneycare.databinding.WalletItemBinding;
import com.example.moneycare.ui.view.transaction.group.GroupMainRvAdapter;
import com.example.moneycare.utils.ImageUtil;

import java.util.List;

public class SelectWalletRvAdapter extends WalletMainRvAdapter {

    public SelectWalletRvAdapter(AppCompatActivity activity, List<Wallet> wallets) {
        super(activity, wallets);
    }

    private void initGroupItemClickEvent(WalletMainRvAdapter.ViewHolder holder, int position){
        Wallet wallet = wallets.get(position);
        holder.walletItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWalletPreference(wallet.id);
                finishActivity();
            }
        });
    }
    private void finishActivity(){
        Intent intent = new Intent();
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
    private void saveWalletPreference(String walletId){
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.transaction_preference_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.current_wallet_key), walletId);
        editor.apply();
    }
    @Override
    public void onBindViewHolder(WalletMainRvAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        initGroupItemClickEvent(holder, position);
    }
}
