package com.example.moneycare.ui.view.transaction;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneycare.data.model.Group;
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
        holder.transactionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = getTransactionBundle(transaction);
                TransactionFragmentDirections.UpdateTransactionAction action = TransactionFragmentDirections.updateTransactionAction(transaction);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }
//    private Bundle getTransactionBundle(UserTransaction transaction){
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("transaction", transaction);
//        return bundle;
//    }

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
        public final LinearLayout transactionItem;

        public ViewHolder(TransactionItemBinding binding) {
            super(binding.getRoot());
            moneyView = binding.transactionMoney;
            dateView = binding.transactionDate;
            transactionItem = binding.transactionItem;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + moneyView.getText() + "'";
        }
    }
}