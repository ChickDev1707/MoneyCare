package com.example.moneycare.ui.view.plan.budget;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.GroupItemBinding;
import com.example.moneycare.utils.ImageLoader;

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

        return new MySelectGroupBudgetRvAdapter.ViewHolder(GroupItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.groupName.setText(transactionGroups.get(position).name);
        ImageLoader imageLoader = new ImageLoader(holder.groupIcon);
        imageLoader.execute(transactionGroups.get(position).image);
        holder.groupItem.setOnClickListener(new View.OnClickListener() {
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
        public final TextView groupName;
        public final ImageView groupIcon;
        public final LinearLayout groupItem;

        public ViewHolder(GroupItemBinding binding) {
            super(binding.getRoot());
            groupName = binding.groupName;
            groupIcon = binding.groupIcon;
            groupItem = binding.groupItem;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + groupName.getText() + "'";
        }
    }
}
