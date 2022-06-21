package com.example.moneycare.ui.view.plan.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.example.moneycare.R;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.repository.EventRepository;
import com.example.moneycare.utils.DateTimeUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {
    private EventRepository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        init();
        initToolBar();
        loadEventList();
        initBtnAddEvent();
    }

    private void init(){
        repository = new EventRepository();
    }

    private void  loadEventList(){
        RecyclerView listEventOngoingRv = findViewById(R.id.event_list_ongoing);
        RecyclerView listEventEndRv = findViewById(R.id.event_list_end);

        LinearLayout layoutContainer = findViewById(R.id.event_container);
        LinearLayout layoutEmpty = findViewById(R.id.event_empty);
        LinearLayout layoutLoading = findViewById(R.id.loading);
        LinearLayout layoutOngoing = findViewById(R.id.event_ongoing);
        LinearLayout layoutEnd = findViewById(R.id.event_end);
        repository.fetchEvents(events ->  {
            layoutLoading.setVisibility(View.INVISIBLE);
            if(events.size() > 0){
                layoutEmpty.setVisibility(View.INVISIBLE);
                layoutContainer.setVisibility(View.VISIBLE);
                List<Event> eventsEnd = new ArrayList<Event>();
                List<Event> eventsOngoing = new ArrayList<Event>();
                for(Event event : (List<Event>)events){
                   if(DateTimeUtil.daysLeft(event.endDate) <= 0 && event.status.equals("ongoing")){
                       repository.changeStatus(event.id, "end");
                       event.status = "end";
                   }
                    if(event.status.equals("end")){
                        eventsEnd.add(event);
                    }
                    else if(event.status.equals("ongoing")){
                        eventsOngoing.add(event);
                    }
                }

                layoutOngoing.setVisibility(eventsOngoing.size() > 0?View.VISIBLE : View.GONE);
                layoutEnd.setVisibility(eventsEnd.size() > 0?View.VISIBLE : View.INVISIBLE);
                listEventEndRv.setAdapter(new MyEventRvAdapter(eventsEnd, EventActivity.this));
                listEventOngoingRv.setAdapter(new MyEventRvAdapter(eventsOngoing, EventActivity.this));
            }
            else {
                layoutContainer.setVisibility(View.INVISIBLE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initToolBar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle("Sự kiện");
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventActivity.this.finish();
                EventActivity.this.onBackPressed();
            }
        });
    }

    private void initBtnAddEvent(){
        FloatingActionButton btnAddEvent = findViewById(R.id.floating_add_btn);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEventList();
    }
}