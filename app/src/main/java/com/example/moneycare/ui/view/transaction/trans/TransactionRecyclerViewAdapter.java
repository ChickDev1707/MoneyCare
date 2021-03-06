package com.example.moneycare.ui.view.transaction.trans;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
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
import com.example.moneycare.utils.appinterface.ReloadTransactionActivity;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder> {

    private List<UserTransaction> transactions;
    private Group group;
    private ReloadTransactionActivity baseActivity;

    public TransactionRecyclerViewAdapter(Group group, List<UserTransaction> items) {
        this.transactions = items;
        this.group = group;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        this.baseActivity = (ReloadTransactionActivity) holder.transactionItem.getContext();
        UserTransaction transaction = transactions.get(position);
        initTransactionMoney(holder.transactionMoney, transaction);

        holder.transactionDate.setText(DateTimeUtil.getDateString(baseActivity, transaction.date));
        holder.transactionDay.setText(Integer.toString(DateTimeUtil.getDay(transaction.date)));
        holder.transactionNote.setText(transaction.note);
        initItemClickEvent(holder, transaction);
        initItemLongClickEvent(holder, transaction);
    }
    private void initItemClickEvent(ViewHolder holder, UserTransaction transaction){
        holder.transactionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateTransaction(transaction);
            }
        });
    }
    private void initItemLongClickEvent(ViewHolder holder, UserTransaction transaction) {
        holder.transactionItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(baseActivity, v, transaction);
                return false;
            }
        });
    }
    public void showPopup(Context context, View v, UserTransaction transaction) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.setGravity(Gravity.CENTER);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.transaction_edit_item){
                    openUpdateTransaction(transaction);
                }else if(item.getItemId() == R.id.transaction_delete_item){
                    TransactionRepository transactionRepository = new TransactionRepository();
                    transactionRepository.deleteTransaction(transaction,
                            data-> {
                                baseActivity.reloadTransactionList();
                                ToastUtil.showToast(baseActivity, "X??a giao d???ch th??nh c??ng");
                            },
                            data-> ToastUtil.showToast(baseActivity, "L???i! X??a giao d???ch th???t b???i"));
                }
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.crud_menu, popup.getMenu());
        popup.show();
    }
    private void openUpdateTransaction(UserTransaction transaction){
        Intent intent = new Intent(baseActivity, UpdateTransactionActivity.class);
        intent.putExtra("transaction", transaction);
        baseActivity.startActivity(intent);
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