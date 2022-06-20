package com.example.moneycare.ui.view.transaction.trans;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.databinding.GroupTransactionItemBinding;
import com.example.moneycare.utils.Converter;
import com.example.moneycare.utils.ImageLoader;
import com.example.moneycare.utils.ImageUtil;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class GroupTransactionRecyclerViewAdapter extends RecyclerView.Adapter<GroupTransactionRecyclerViewAdapter.ViewHolder> {

    private List<GroupTransaction> groupTransactions;

    public GroupTransactionRecyclerViewAdapter(List<GroupTransaction> items) {
        this.groupTransactions = items;
    }

    public void GroupTransactionItemBinding(List<GroupTransaction> newGroupTransList){
        this.groupTransactions.clear();
        this.groupTransactions = newGroupTransList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(GroupTransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Context context = holder.groupNameView.getContext();
        GroupTransaction groupTransaction = groupTransactions.get(position);
        holder.groupNameView.setText(groupTransaction.group.name);

        String totalTransFullText = Integer.toString(groupTransaction.transactionList.size()) + " giao dịch";
        holder.totalTransaction.setText(totalTransFullText);
        holder.totalMoney.setText(Converter.toFormattedMoney(context, groupTransaction.getTotalMoney()));

        ImageLoader imageLoader = new ImageLoader(holder.groupIcon);
        imageLoader.execute(groupTransaction.group.image);
        holder.transactionListView.setAdapter(new TransactionRecyclerViewAdapter(groupTransaction.group, groupTransaction.transactionList));
    }

    @Override
    public int getItemCount() {
        return groupTransactions.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView groupNameView;
        public final ImageView groupIcon;
        public final TextView totalTransaction;
        public final TextView totalMoney;
        public final RecyclerView transactionListView;

        public ViewHolder(GroupTransactionItemBinding binding) {
            super(binding.getRoot());
            groupNameView = binding.groupName;
            groupIcon = binding.groupIcon;
            totalTransaction = binding.totalTransaction;
            totalMoney = binding.totalMoney;
            transactionListView = binding.transactionListTemplate;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + groupNameView.getText() + "'";
        }
    }
}