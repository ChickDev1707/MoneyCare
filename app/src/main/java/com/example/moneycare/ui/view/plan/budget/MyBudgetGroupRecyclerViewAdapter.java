package com.example.moneycare.ui.view.plan.budget;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.GroupItemBinding;
import com.example.moneycare.ui.viewmodel.plan.BudgetViewModel;
import com.example.moneycare.utils.DateTimeUtil;
import com.example.moneycare.utils.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyBudgetGroupRecyclerViewAdapter extends RecyclerView.Adapter<MyBudgetGroupRecyclerViewAdapter.ViewHolder> {

    private List<Group> transactionGroups = new ArrayList<Group>();
    private List<Budget> budgets = new ArrayList<Budget>();
//    public ActivityResultLauncher<Intent> toDetailBudgetActivity;
    public BudgetViewModel budgetsVM;

    public MyBudgetGroupRecyclerViewAdapter() {
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public void setTransactionGroups(List<Group> transactionGroups) {
        this.transactionGroups = transactionGroups;
    }

    @Override
    public MyBudgetGroupRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyBudgetGroupRecyclerViewAdapter.ViewHolder(GroupItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.groupName.setText(transactionGroups.get(position).name);
        holder.groupCreatedDate.setText(DateTimeUtil.getDateString(holder.groupName.getContext(),budgets.get(position).getDate()));
        ImageLoader imageLoader = new ImageLoader(holder.groupIcon);
        imageLoader.execute(transactionGroups.get(position).image);
        holder.groupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BudgetDetailActivity.class);
                intent.putExtra("imgGroup", transactionGroups.get(position).image);
                intent.putExtra("groupName", transactionGroups.get(position).name);
                intent.putExtra("idBudget", budgets.get(position).getId());
                intent.putExtra("totalBudget", budgetsVM.totalBudget.getValue());
                holder.groupItem.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return transactionGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView groupName;
        public final TextView groupCreatedDate;
        public final ImageView groupIcon;
        public final View groupItem;

        public ViewHolder(GroupItemBinding binding) {
            super(binding.getRoot());
            groupName = binding.groupName;
            groupIcon = binding.groupIcon;
            groupItem = binding.groupItem;
            groupCreatedDate = binding.groupType;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + groupName.getText() + "'";
        }
    }
}
