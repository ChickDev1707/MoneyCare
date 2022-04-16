package com.example.moneycare.ui.view.transaction;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.FragmentGroupBinding;

import java.util.List;

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder> {

    private final List<Group> groups;
    private View view;

    public GroupRecyclerViewAdapter(View view, List<Group> groups) {
        this.groups = groups;
        this.view = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Group group = groups.get(position);
        Glide.with(view).load(group.imgUrl).into(holder.groupIcon);
        holder.groupName.setText(group.name);
        initGroupItemClickEvent(holder, group);
    }
    private void initGroupItemClickEvent(ViewHolder holder, Group group){
        holder.groupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                Bundle bundle = getGroupBundle(group);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("bundle", bundle);
                navController.popBackStack();
            }
        });
    }
    private Bundle getGroupBundle(Group group){
        Bundle bundle = new Bundle();
        bundle.putParcelable("group", group);
        return bundle;
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout groupItem;
        public final ImageView groupIcon;
        public final TextView groupName;

        public ViewHolder(FragmentGroupBinding binding) {
            super(binding.getRoot());
            groupItem = binding.groupItem;
            groupIcon = binding.groupIcon;
            groupName = binding.groupName;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + groupName.getText() + "'";
        }
    }
}