package com.example.moneycare.ui.view.transaction.trans;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.EventItemBinding;
import com.example.moneycare.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class SelectEventRvAdapter extends RecyclerView.Adapter<SelectEventRvAdapter.ViewHolder>{
    private List<Event> events = new ArrayList<Event>();
    TransactionRepository repository;

    AppCompatActivity activity;
    public SelectEventRvAdapter(AppCompatActivity activity, List<Event> events) {
        this.events = events;
        this.activity = activity;
        repository = new TransactionRepository();
    }
    @Override
    public SelectEventRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SelectEventRvAdapter.ViewHolder(EventItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventName.setText(events.get(position).name);
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
                holder.money.setText(total.toString());
                holder.lbMoney.setText("Đã chi:");
            }
            else{
                holder.money.setText(total.toString());
                holder.lbMoney.setText("Thu vào:");
            }
        });

        if(events.get(position).image != ""){
            ImageLoader imageLoader = new ImageLoader(holder.imgView);
            imageLoader.execute(events.get(position).image);
        }
        holder.eventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("event", event);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
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
        public final RelativeLayout eventItem;

        public ViewHolder(EventItemBinding binding) {
            super(binding.getRoot());
            eventItem = binding.itemContainer;
            eventName = binding.eventName;
            imgView = binding.imgItem;
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
