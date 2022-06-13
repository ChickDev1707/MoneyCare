package com.example.moneycare.ui.view.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.R;
import com.example.moneycare.databinding.AccountPanelItemBinding;
import com.example.moneycare.ui.view.IntroActivity;
import com.example.moneycare.ui.view.transaction.group.ManageGroupActivity;
import com.example.moneycare.ui.view.transaction.wallet.ManageWalletActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class AccountItemRecyclerViewAdapter extends RecyclerView.Adapter<AccountItemRecyclerViewAdapter.ViewHolder> {

    private List<AccountItem> accountItems;
    private Activity currentActivity;

    public AccountItemRecyclerViewAdapter(Activity activity) {
        currentActivity = activity;
        initAccountItems();
    }
    private void initAccountItems(){
        this.accountItems = new ArrayList<>();
        accountItems.add(new AccountItem(R.drawable.ic_account_balance_wallet_24, "Quản lý ví"));
        accountItems.add(new AccountItem(R.drawable.ic_group_icon_24, "Quản lý nhóm"));
        accountItems.add(new AccountItem(R.drawable.ic_settings_24, "Cài đặt"));
        accountItems.add(new AccountItem(R.drawable.ic_help_24, "Hỗ trợ"));
        accountItems.add(new AccountItem(R.drawable.ic_info_24, "Giới thiệu"));
        accountItems.add(new AccountItem(R.drawable.ic_logout_24, "Đăng xuất"));
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(AccountPanelItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AccountItem accountItem = accountItems.get(position);

        holder.accountItem.setOnClickListener(getAccountItemClickEvent(position));
        holder.itemImage.setImageResource(accountItem.image);
        holder.itemName.setText(accountItem.name);

        if(position == accountItems.size() - 1){
            holder.itemName.setTextColor(ContextCompat.getColor(currentActivity, R.color.red_main));
        }
    }
    private View.OnClickListener getAccountItemClickEvent(int position){
        switch (position){
            case 0:
                return getNavigateToMangeWalletClickListener();
            case 1:
                return getNavigateToMangeGroupClickListener();
            case 2:
                return getNavigateToSettingsClickListener();
            case 5:
                return getSignOutClickListener();
            default:
                return null;
        }
    }
    private View.OnClickListener getNavigateToMangeWalletClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentActivity, ManageWalletActivity.class);
                currentActivity.startActivity(intent);
            }
        };
    }
    private View.OnClickListener getNavigateToMangeGroupClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentActivity, ManageGroupActivity.class);
                currentActivity.startActivity(intent);
            }
        };
    }
    private View.OnClickListener getNavigateToSettingsClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentActivity, SettingsActivity.class);
                currentActivity.startActivity(intent);
            }
        };
    }
    private View.OnClickListener getSignOutClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutByAuthUI();
                clearTransactionSetting();
            }
        };
    }
    private void signOutByAuthUI(){
        AuthUI.getInstance()
        .signOut(currentActivity)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                Intent i = new Intent(currentActivity, IntroActivity.class);
                currentActivity.startActivity(i);
            }
        });
    }
    private void clearTransactionSetting(){
        SharedPreferences sharedPref = currentActivity.getSharedPreferences(currentActivity.getString(R.string.app_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return accountItems.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView itemImage;
        public final TextView itemName;
        public final LinearLayout accountItem;
        
        public ViewHolder(AccountPanelItemBinding binding) {
            super(binding.getRoot());
            itemImage = binding.itemImg;
            itemName = binding.itemName;
            accountItem = binding.accountItem;
        }
        @Override
        public String toString() {
            return super.toString() + " '" + itemName.getText() + "'";
        }
    }
    public class AccountItem{
        public int image;
        public String name;
        public AccountItem(int image, String name){
            this.image = image;
            this.name = name;
        }
    }
}