package com.example.moneycare.ui.view.report;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.databinding.ReportItemBinding;
import com.example.moneycare.utils.ImageUtil;

import java.util.List;

public class ReportIncomeRecyclerViewAdapter extends  RecyclerView.Adapter<ReportIncomeRecyclerViewAdapter.ViewHoldel>{

    private List<GroupTransaction> groupTransactionIncomeList;
    private long totalMoney;

    public ReportIncomeRecyclerViewAdapter(List<GroupTransaction> groupTransactionIncomeList){
        this.groupTransactionIncomeList = groupTransactionIncomeList;
        this.totalMoney = 0;
        for (GroupTransaction groupTransaction:groupTransactionIncomeList){
            totalMoney+=groupTransaction.getTotalMoney();
        }
    }

    @NonNull
    @Override
    public ViewHoldel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHoldel(ReportItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoldel holder, int position) {
        GroupTransaction groupTransaction = groupTransactionIncomeList.get(position);
        Bitmap bitmapIcon = ImageUtil.toBitmap(groupTransaction.group.image);
        holder.groupIcon.setImageBitmap(bitmapIcon);
        holder.groupName.setText(groupTransaction.group.name);
        holder.totalMoney.setText(Long.toString(groupTransaction.getTotalMoney()));
        holder.percentMoney.setText(String.format("%.1f", (float)(groupTransaction.getTotalMoney()*100.0/this.totalMoney))+"%");
    }

    @Override
    public int getItemCount() {
        if (this.groupTransactionIncomeList == null)
            return 0;
        return this.groupTransactionIncomeList.size();
    }

    public class ViewHoldel extends RecyclerView.ViewHolder {
        public final ImageView groupIcon;
        public final TextView groupName;
        public final TextView percentMoney;
        public final TextView totalMoney;
        public final LinearLayout reportItem;


        public ViewHoldel(@NonNull ReportItemBinding binding) {
            super(binding.getRoot());
            this.groupIcon = binding.groupIcon;
            this.groupName = binding.groupName;
            this.percentMoney = binding.percentMoney;
            this.totalMoney = binding.totalMoney;
            this.reportItem = binding.reportItem;
        }
    }
}
