package com.example.moneycare.ui.view.plan.event;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.FragmentBudgetsGroupItemBinding;
import com.example.moneycare.databinding.FragmentEventItemBinding;
import com.example.moneycare.ui.view.plan.budget.BudgetDetailActivity;
import com.example.moneycare.ui.viewmodel.plan.BudgetViewModel;
import com.example.moneycare.utils.Convert;
import com.example.moneycare.utils.DateTimeUtil;
import com.example.moneycare.utils.LoadImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyEventRvAdapter extends RecyclerView.Adapter<MyEventRvAdapter.ViewHolder> {


    private List<Event> events = new ArrayList<Event>();
    private EventActivity activity;
    public MyEventRvAdapter() {

    }
    public ActivityResultLauncher<Intent> toDetailEventActivity;
    public MyEventRvAdapter(List<Event> events, EventActivity activity,
                            ActivityResultLauncher<Intent> launcher) {
        this.events = events;
        this.activity =activity;
        this.toDetailEventActivity = launcher;

    }
    @Override
    public MyEventRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyEventRvAdapter.ViewHolder(FragmentEventItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.eventName.setText(events.get(position).name);
        holder.daysLeft.setText("Còn " + DateTimeUtil.daysLeft(events.get(position).endDate) + " ngày");
//        holder.spent.setText(Convert.convertToThousandsSeparator());
        if(events.get(position).image != ""){
            LoadImage loadImage = new LoadImage(holder.imgView);
            loadImage.execute(events.get(position).image);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EventDetailActivity.class);
                intent.putExtra("eventSelected", events.get(position));
                toDetailEventActivity.launch(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView eventName;
        public final TextView daysLeft;
        public final ImageView imgView;
        public final TextView spent;
        public final RelativeLayout relativeLayout;

        public ViewHolder(FragmentEventItemBinding binding) {
            super(binding.getRoot());
            eventName = binding.eventName;
            imgView = binding.imgItem;
            relativeLayout = binding.itemContainer;
            spent = binding.spent;
            daysLeft = binding.daysLeft;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + eventName.getText() + "'";
        }
    }
}
