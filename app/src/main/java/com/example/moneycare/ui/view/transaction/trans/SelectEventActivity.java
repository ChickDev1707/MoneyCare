package com.example.moneycare.ui.view.transaction.trans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.repository.EventRepository;
import com.example.moneycare.databinding.ActivitySelectEventBinding;

import java.util.ArrayList;
import java.util.List;

public class SelectEventActivity extends AppCompatActivity {

    EventRepository repository;
    ActivitySelectEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySelectEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = new EventRepository();

        repository.fetchEvents(events -> {
            List<Event> eventsOngoing = new ArrayList<Event>();
            for(Event event : (List<Event>)events){
                 if(event.status.equals("ongoing")){
                    eventsOngoing.add(event);
                }
            }
            binding.eventOngoingList.setAdapter(new SelectEventRvAdapter(this, eventsOngoing));
        });
        initToolbar();
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle("Chọn sự kiện");
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
    }
}