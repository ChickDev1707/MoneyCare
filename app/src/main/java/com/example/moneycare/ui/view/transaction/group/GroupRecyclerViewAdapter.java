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

import com.bumptech.glide.Glide;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.GroupItemBinding;
import com.example.moneycare.utils.ImageUtil;

import java.util.List;

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder> {

    private final List<Group> groups;
    private final AppCompatActivity activity;

    public GroupRecyclerViewAdapter(AppCompatActivity activity, List<Group> groups) {
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

        Bitmap bitmapIcon = ImageUtil.toBitmap(group.image);
        holder.groupIcon.setImageBitmap(bitmapIcon);
        holder.groupName.setText(group.name);
        initGroupItemClickEvent(holder, group);
    }
    private void initGroupItemClickEvent(ViewHolder holder, Group group){
        holder.groupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("group", group);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout groupItem;
        public final ImageView groupIcon;
        public final TextView groupName;

        public ViewHolder(GroupItemBinding binding) {
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
