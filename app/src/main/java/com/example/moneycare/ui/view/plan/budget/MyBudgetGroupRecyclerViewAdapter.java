package com.example.moneycare.ui.view.plan.budget;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.FragmentBudgetsGroupItemBinding;
import com.example.moneycare.ui.viewmodel.plan.BudgetViewModel;
import com.example.moneycare.utils.LoadImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyBudgetGroupRecyclerViewAdapter extends RecyclerView.Adapter<MyBudgetGroupRecyclerViewAdapter.ViewHolder> {

    private List<Group> transactionGroups = new ArrayList<Group>();
    private List<Budget> budgets = new ArrayList<Budget>();
    public ActivityResultLauncher<Intent> toDetailBudgetActivity;
    public BudgetViewModel budgetsVM;

    public MyBudgetGroupRecyclerViewAdapter() {
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public List<Group> getTransactionGroups() {
        return transactionGroups;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public void setTransactionGroups(List<Group> transactionGroups) {
        this.transactionGroups = transactionGroups;
    }

    @Override
    public MyBudgetGroupRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyBudgetGroupRecyclerViewAdapter.ViewHolder(FragmentBudgetsGroupItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = formatter.format(budgets.get(position).getDate());
        holder.itemName.setText(transactionGroups.get(position).name);
        holder.itemDateCreated.setText(strDate);
        LoadImage loadImage = new LoadImage(holder.imgView);
        loadImage.execute(transactionGroups.get(position).image);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), BudgetDetailActivity.class);
                i.putExtra("imgGroup", transactionGroups.get(position).image);
                i.putExtra("groupName", transactionGroups.get(position).name);
                i.putExtra("idBudget", budgets.get(position).getId());
                i.putExtra("totalBudget", budgetsVM.totalBudgetImpl);
                toDetailBudgetActivity.launch(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return transactionGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemName;
        public final TextView itemDateCreated;
        public final ImageView imgView;
        public final RelativeLayout relativeLayout;

        public ViewHolder(FragmentBudgetsGroupItemBinding binding) {
            super(binding.getRoot());
            itemName = binding.budgetGroupName;
            imgView = binding.imgItem;
            relativeLayout = binding.itemContainer;
            itemDateCreated = binding.budgetDateCreated;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemName.getText() + "'";
        }
    }
}
