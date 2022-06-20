package com.example.moneycare.ui.view.transaction.trans;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.TransactionItemBinding;
import com.example.moneycare.ui.view.MainActivity;
import com.example.moneycare.utils.Converter;
import com.example.moneycare.utils.DateTimeUtil;
import com.example.moneycare.utils.ToastUtil;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder> {

    private List<UserTransaction> transactions;
    private Group           group;
    private MainActivity        activity;
    private UserTransaction transaction;

    public TransactionRecyclerViewAdapter(Group group, List<UserTransaction> items) {
        this.transactions = items;
        this.group = group;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        activity = (MainActivity) viewHolder.itemView.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        transaction = transactions.get(position);
        initTransactionMoney(holder.transactionMoney, transaction);

        holder.transactionDate.setText(DateTimeUtil.getDateString(activity, transaction.date));
        holder.transactionDay.setText(Integer.toString(DateTimeUtil.getDay(transaction.date)));
        holder.transactionNote.setText(transaction.note);
        initItemClickEvent(holder);
        initItemLongClickEvent(holder);
    }
    private void initItemClickEvent(ViewHolder holder){
        holder.transactionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateTransaction();
            }
        });
    }
    private void initItemLongClickEvent(ViewHolder holder) {
        holder.transactionItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(activity, v);
                return false;
            }
        });
    }
    private void openUpdateTransaction(){
        Intent intent = new Intent(activity, UpdateTransactionActivity.class);
        intent.putExtra("transaction", transaction);
        activity.startActivity(intent);
    }
    public void showPopup(Context context, View v) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setGravity(Gravity.CENTER);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.transaction_edit_item){
                    openUpdateTransaction();
                }else if(item.getItemId() == R.id.transaction_delete_item){
                    TransactionRepository transactionRepository = new TransactionRepository();
                    transactionRepository.deleteTransaction(transaction,
                            data-> {
                                reloadTransactionList();
                                ToastUtil.showToast(activity, "Xóa giao dịch thành công");
                            },
                            data-> ToastUtil.showToast(activity, "Lỗi! Xóa giao dịch thất bại"));
                }
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.crud_menu, popup.getMenu());
        popup.show();
    }
    private void reloadTransactionList(){
        Fragment navHostFragment = activity.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        Fragment firstFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        if(firstFragment.getClass().equals(TransactionFragment.class)){
            TransactionFragment fragment = (TransactionFragment) firstFragment;
            fragment.initElements();
        }
    }
    private void initTransactionMoney(TextView transactionMoney, UserTransaction transaction){
        Context context = transactionMoney.getContext();
        transactionMoney.setText(Converter.toFormattedMoney(context, transaction.money));
        int color = group.type? ContextCompat.getColor(context, R.color.green_main) : ContextCompat.getColor(context, R.color.red_main);
        transactionMoney.setTextColor(color);
    }
    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView transactionMoney;
        public final TextView transactionDate;
        public final TextView transactionDay;
        public final TextView transactionNote;
        public final LinearLayout transactionItem;

        public ViewHolder(TransactionItemBinding binding) {
            super(binding.getRoot());
            transactionMoney = binding.transactionMoney;
            transactionDate = binding.transactionDate;
            transactionDay = binding.transactionDay;
            transactionNote = binding.transactionNote;
            transactionItem = binding.transactionItem;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + transactionMoney.getText() + "'";
        }
    }
}