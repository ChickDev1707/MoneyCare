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

import com.example.moneycare.data.model.Event;
import com.example.moneycare.databinding.FragmentEventItemBinding;

import java.util.ArrayList;
import java.util.List;

public class SelectEventRvAdapter extends RecyclerView.Adapter<SelectEventRvAdapter.ViewHolder>{
    private List<Event> events = new ArrayList<Event>();

    AppCompatActivity activity;
    public SelectEventRvAdapter(AppCompatActivity activity, List<Event> events) {
        this.events = events;
        this.activity = activity;
    }
    @Override
    public SelectEventRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SelectEventRvAdapter.ViewHolder(FragmentEventItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventName.setText(events.get(position).name);
        holder.daysLeft.setText("remaining");

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
        public final TextView spent;
        public final RelativeLayout eventItem;

        public ViewHolder(FragmentEventItemBinding binding) {
            super(binding.getRoot());
            eventItem = binding.itemContainer;
            eventName = binding.eventName;
            imgView = binding.imgItem;
            spent = binding.spent;
            daysLeft = binding.daysLeft;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + eventName.getText() + "'";
        }
    }
}
