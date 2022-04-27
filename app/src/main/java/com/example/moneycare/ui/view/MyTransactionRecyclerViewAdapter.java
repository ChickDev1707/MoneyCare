package com.example.moneycare.ui.view;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moneycare.data.model.Transaction;
import com.example.moneycare.databinding.FragmentTransactionBinding;
import com.example.moneycare.utils.DateUtil;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTransactionRecyclerViewAdapter extends RecyclerView.Adapter<MyTransactionRecyclerViewAdapter.ViewHolder> {

    private List<Transaction> transactions;

    public MyTransactionRecyclerViewAdapter(List<Transaction> items) {
        transactions = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.moneyView.setText(Double.toString(transactions.get(position).money));
        holder.dateView.setText(DateUtil.getStringDate(transactions.get(position).date));
    }
    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void updateTransactionList(List<Transaction> newTransList){
        this.transactions.clear();
        this.transactions = newTransList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView moneyView;
        public final TextView dateView;
        public Transaction transaction;

        public ViewHolder(FragmentTransactionBinding binding) {
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