package com.example.moneycare.ui.view.plan;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.FragmentSelectGroupBudgetItemBinding;
import com.example.moneycare.utils.LoadImage;

import java.util.ArrayList;
import java.util.List;

public class MySelectGroupBudgetRvAdapter extends RecyclerView.Adapter<MySelectGroupBudgetRvAdapter.ViewHolder> {

    private List<Group> transactionGroups = new ArrayList<Group>();
    private SelectBudgetGroupActivity activity;

    public MySelectGroupBudgetRvAdapter() {
    }

    public MySelectGroupBudgetRvAdapter(SelectBudgetGroupActivity activity, List<Group> groups) {
        this.activity = activity;
        this.transactionGroups = groups;
    }


    @Override
    public MySelectGroupBudgetRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MySelectGroupBudgetRvAdapter.ViewHolder(FragmentSelectGroupBudgetItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemName.setText(transactionGroups.get(position).name);
        LoadImage loadImage = new LoadImage(holder.imgView);
        loadImage.execute(transactionGroups.get(position).image);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("group", transactionGroups.get(position));
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
    }
    @Override
    public int getItemCount() {
        return transactionGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView itemName;
        public final ImageView imgView;
        public final RelativeLayout relativeLayout;

        public ViewHolder(FragmentSelectGroupBudgetItemBinding binding) {
            super(binding.getRoot());
            itemName = binding.budgetSelectGroupName;
            imgView = binding.imgSelectGroupBudgetItem;
            relativeLayout = binding.itemContainer;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemName.getText() + "'";
        }
    }
}
