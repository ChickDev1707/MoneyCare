package com.example.moneycare.ui.view.transaction;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.databinding.TransactionItemBinding;
import com.example.moneycare.utils.DateUtil;
import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder> {

    private List<UserTransaction> transactions;

    public TransactionRecyclerViewAdapter(List<UserTransaction> items) {
        transactions = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        UserTransaction transaction = transactions.get(position);
        holder.moneyView.setText(Long.toString(transaction.money));
        holder.dateView.setText(DateUtil.getStringDate(transaction.date));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void updateTransactionList(List<UserTransaction> newTransList){
        this.transactions.clear();
        this.transactions = newTransList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView moneyView;
        public final TextView dateView;

        public ViewHolder(TransactionItemBinding binding) {
            super(binding.getRoot());
            moneyView = binding.transactionMoney;
            dateView = binding.transactionDate;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + moneyView.getText() + "'";
        }
    }
}