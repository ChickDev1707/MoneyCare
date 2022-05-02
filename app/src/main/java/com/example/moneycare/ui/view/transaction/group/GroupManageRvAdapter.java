package com.example.moneycare.ui.view.transaction.group;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneycare.data.model.Group;

import java.util.List;

public class GroupManageRvAdapter extends GroupMainRvAdapter{
    public GroupManageRvAdapter(AppCompatActivity activity, List<Group> groups) {
        super(activity, groups);
    }
    private void initGroupItemClickEvent(ViewHolder holder, int position){
        Group group = groups.get(position);
        holder.groupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!group.isDefault) startUpdateGroup(group);
            }
        });
    }
    private void startUpdateGroup(Group group){
        Intent intent = new Intent(activity, UpdateGroupActivity.class);
        intent.putExtra("group", group);
        activity.startActivity(intent);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        initGroupItemClickEvent(holder, position);
    }
}
