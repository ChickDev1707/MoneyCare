package com.example.moneycare.ui.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.TransactionGroup;
import com.example.moneycare.databinding.FragmentBudgetsGroupItemBinding;
import com.example.moneycare.generated.callback.OnClickListener;
import com.example.moneycare.ui.viewmodel.BudgetViewModel;
import com.example.moneycare.utils.Convert;
import com.example.moneycare.utils.LoadImage;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyBudgetGroupRecyclerViewAdapter extends RecyclerView.Adapter<MyBudgetGroupRecyclerViewAdapter.ViewHolder> {

    private List<TransactionGroup> transactionGroups = new ArrayList<TransactionGroup>();
    private List<Budget> budgets = new ArrayList<Budget>();

    public MyBudgetGroupRecyclerViewAdapter() {
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public List<TransactionGroup> getTransactionGroups() {
        return transactionGroups;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public void setTransactionGroups(List<TransactionGroup> transactionGroups) {
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
        holder.itemName.setText(transactionGroups.get(position).getName());
        holder.itemDateCreated.setText(strDate);
        LoadImage loadImage = new LoadImage(holder.imgView);
        loadImage.execute(transactionGroups.get(position).getImage());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("idBudget", budgets.get(position).getId());
                bundle.putString("imgGroup", transactionGroups.get(position).getImage());
                bundle.putString("groupName", transactionGroups.get(position).getName());
                Navigation.findNavController(v).navigate(R.id.action_budgetFragment_to_budgetDetailFragment, bundle);
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
