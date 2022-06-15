package com.example.moneycare.ui.view.transaction.trans;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.databinding.TransactionItemBinding;
import com.example.moneycare.ui.view.MainActivity;
import com.example.moneycare.utils.Converter;
import com.example.moneycare.utils.DateTimeUtil;
import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder> {

    private List<UserTransaction> transactions;
    private Group group;

    public TransactionRecyclerViewAdapter(Group group, List<UserTransaction> items) {
        this.transactions = items;
        this.group = group;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        UserTransaction transaction = transactions.get(position);
        initTransactionMoney(holder.transactionMoney, transaction);

        Context context = holder.itemView.getContext();

        holder.transactionDate.setText(DateTimeUtil.getDateString(context, transaction.date));
        holder.transactionDay.setText(Integer.toString(DateTimeUtil.getDay(transaction.date)));
        holder.transactionNote.setText(transaction.note);
        holder.transactionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity context = (MainActivity) holder.transactionDate.getContext();
                ActivityResultLauncher<Intent> launcher = context.getReloadTransFragmentLauncher();

                Intent intent = new Intent(context, UpdateTransactionActivity.class);
                intent.putExtra("transaction", transaction);
                launcher.launch(intent);
            }
        });
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

    public void updateTransactionList(List<UserTransaction> newTransList){
        this.transactions.clear();
        this.transactions = newTransList;
        notifyDataSetChanged();
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