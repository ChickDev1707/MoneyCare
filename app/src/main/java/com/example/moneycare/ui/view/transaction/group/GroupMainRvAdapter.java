package com.example.moneycare.ui.view.transaction.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.GroupItemBinding;
import com.example.moneycare.utils.ImageLoader;
import com.example.moneycare.utils.ImageUtil;

import java.util.List;

public class GroupMainRvAdapter extends RecyclerView.Adapter<GroupMainRvAdapter.ViewHolder> {

    protected List<Group> groups;
    protected AppCompatActivity activity;

    public GroupMainRvAdapter(AppCompatActivity activity, List<Group> groups) {
        this.groups = groups;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(GroupItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Group group = groups.get(position);

        ImageLoader imageLoader = new ImageLoader(holder.groupIcon);
        imageLoader.execute(group.image);
        holder.groupName.setText(group.name);

        String groupType = group.isDefault? "" : "C";
        holder.groupType.setText(groupType);
    }


    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout groupItem;
        public final ImageView groupIcon;
        public final TextView groupName;
        public final TextView groupType;

        public ViewHolder(GroupItemBinding binding) {
            super(binding.getRoot());
            groupItem = binding.groupItem;
            groupIcon = binding.groupIcon;
            groupName = binding.groupName;
            groupType = binding.groupType;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + groupName.getText() + "'";
        }
    }
}
