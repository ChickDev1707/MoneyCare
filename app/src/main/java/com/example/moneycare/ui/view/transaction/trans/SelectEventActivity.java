package com.example.moneycare.ui.view.transaction.trans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
            List<Event> eventsEnd = new ArrayList<Event>();
            List<Event> eventsOngoing = new ArrayList<Event>();
            for(Event event : (List<Event>)events){
                if(event.status.equals("end")){
                    eventsEnd.add(event);
                }
                else if(event.status.equals("ongoing")){
                    eventsOngoing.add(event);
                }
            }

            binding.eventEndedList.setAdapter(new SelectEventRvAdapter(this, eventsEnd));
            binding.eventOngoingList.setAdapter(new SelectEventRvAdapter(this, eventsOngoing));
        });
    }
}