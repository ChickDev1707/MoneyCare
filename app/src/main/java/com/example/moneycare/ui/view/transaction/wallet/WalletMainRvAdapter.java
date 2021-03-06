package com.example.moneycare.ui.view.transaction.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.databinding.WalletItemBinding;
import com.example.moneycare.utils.Converter;
import com.example.moneycare.utils.ImageLoader;

import java.util.List;

public class WalletMainRvAdapter extends RecyclerView.Adapter<WalletMainRvAdapter.ViewHolder> {

    protected List<Wallet> wallets;
    protected AppCompatActivity activity;

    public WalletMainRvAdapter(AppCompatActivity activity, List<Wallet> wallets) {
        this.wallets = wallets;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(WalletItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Wallet wallet = wallets.get(position);

        holder.walletName.setText(wallet.name);
        holder.walletMoney.setText(Converter.toFormattedMoney(holder.walletName.getContext(), wallet.money));

        ImageLoader imageLoader = new ImageLoader(holder.walletIcon);
        imageLoader.execute(wallet.image);
    }


    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout walletItem;
        public final TextView walletName;
        public final TextView walletMoney;
        public final ImageView walletIcon;

        public ViewHolder(WalletItemBinding binding) {
            super(binding.getRoot());
            walletName = binding.walletName;
            walletMoney = binding.walletMoney;
            walletIcon = binding.walletIcon;
            walletItem = binding.walletItem;    
        }

        @Override
        public String toString() {
            return super.toString() + " '" + walletName.getText() + "'";
        }
    }
}
