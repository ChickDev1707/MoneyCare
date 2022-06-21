package com.example.moneycare.ui.view.plan.event;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.EventItemBinding;
import com.example.moneycare.utils.Converter;
import com.example.moneycare.utils.DateTimeUtil;
import com.example.moneycare.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MyEventRvAdapter extends RecyclerView.Adapter<MyEventRvAdapter.ViewHolder> {

    private List<Event> events = new ArrayList<Event>();
    private EventActivity         activity;
    private TransactionRepository repository;

    public MyEventRvAdapter(List<Event> events, EventActivity activity) {
        this.events = events;
        this.activity =activity;
        repository = new TransactionRepository();
    }
    @Override
    public MyEventRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyEventRvAdapter.ViewHolder(EventItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.eventName.setText(events.get(position).name);
        holder.daysLeft.setText("Còn " + DateTimeUtil.daysLeft(events.get(position).endDate) + " ngày");
        repository.getTransactionsByEvent(events.get(position).id, groupTransactions -> {
            Long total = 0L;
          for (GroupTransaction groupTransaction : groupTransactions){
              if(groupTransaction.group.type == true){
                  total += groupTransaction.getTotalMoney();
              }else {
                  total -= groupTransaction.getTotalMoney();
              }
          }
          if(total <= 0) {
              total = -total;
              holder.money.setText(Converter.toFormattedMoney(holder.money.getContext(), total));
              holder.lbMoney.setText("Đã chi:");
          }
          else{
              holder.money.setText(Converter.toFormattedMoney(holder.money.getContext(), total));
              holder.lbMoney.setText("Thu vào:");
          }
        });
        if(events.get(position).image != ""){
            ImageLoader imageLoader = new ImageLoader(holder.imgView);
            imageLoader.execute(events.get(position).image);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EventDetailActivity.class);
                intent.putExtra("eventSelected", events.get(position));
                activity.startActivity(intent);
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
        public final TextView money;
        public final TextView lbMoney;
        public final RelativeLayout relativeLayout;

        public ViewHolder(EventItemBinding binding) {
            super(binding.getRoot());
            eventName = binding.eventName;
            imgView = binding.imgItem;
            relativeLayout = binding.itemContainer;
            money = binding.money;
            lbMoney = binding.lbMoney;
            daysLeft = binding.daysLeft;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + eventName.getText() + "'";
        }
    }
}
