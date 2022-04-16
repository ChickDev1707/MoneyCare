package com.example.moneycare.ui.view.transaction;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.databinding.GroupTransactionItemBinding;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class GroupTransactionRecyclerViewAdapter extends RecyclerView.Adapter<GroupTransactionRecyclerViewAdapter.ViewHolder> {

    private List<GroupTransaction> groupTransactions;

    public GroupTransactionRecyclerViewAdapter(List<GroupTransaction> items) {
        groupTransactions = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(GroupTransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GroupTransaction groupTransaction = groupTransactions.get(position);
        holder.groupNameView.setText(groupTransaction.group.name);
        holder.transactionListView.setAdapter(new TransactionRecyclerViewAdapter(groupTransaction.transactionList));
    }

    @Override
    public int getItemCount() {
        return groupTransactions.size();
    }

    public void GroupTransactionItemBinding(List<GroupTransaction> newGroupTransList){
        this.groupTransactions.clear();
        this.groupTransactions = newGroupTransList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView groupNameView;
        public final RecyclerView transactionListView;

        public ViewHolder(GroupTransactionItemBinding binding) {
            super(binding.getRoot());
            groupNameView = binding.groupName;
            transactionListView = binding.transactionListTemplate;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + groupNameView.getText() + "'";
        }
    }
}