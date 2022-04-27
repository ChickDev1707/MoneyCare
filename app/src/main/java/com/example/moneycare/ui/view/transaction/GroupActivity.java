package com.example.moneycare.ui.view.transaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.moneycare.R;
import com.example.moneycare.data.repository.TransactionRepository;

public class GroupActivity extends AppCompatActivity {
    private TransactionRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        repository = new TransactionRepository();
        // Set the adapter
        RecyclerView recyclerView = findViewById(R.id.group_list);
        repository.fetchGroups(groups-> recyclerView.setAdapter(new GroupRecyclerViewAdapter(this, groups)));

    }
}