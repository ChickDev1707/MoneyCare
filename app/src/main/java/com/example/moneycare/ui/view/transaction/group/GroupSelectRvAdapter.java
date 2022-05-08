package com.example.moneycare.ui.view.transaction.group;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneycare.data.model.Group;

import java.util.List;

public class GroupSelectRvAdapter extends GroupMainRvAdapter{

    public GroupSelectRvAdapter(AppCompatActivity activity, List<Group> groups) {
        super(activity, groups);
    }
    private void initGroupItemClickEvent(ViewHolder holder, int position){
        Group group = groups.get(position);
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        initGroupItemClickEvent(holder, position);
    }
}
