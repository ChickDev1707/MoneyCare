package com.example.moneycare.ui.view.plan.budget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.GroupRepository;
import com.example.moneycare.databinding.ActivitySelectBudgetGroupBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectBudgetGroupActivity extends AppCompatActivity {

    private ActivitySelectBudgetGroupBinding binding;
    private GroupRepository repository;
    private List<String> activeGroups = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectBudgetGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        activeGroups = intent.getStringArrayListExtra("activeGroups");


        repository = new GroupRepository();

        initToolbar();
        loadData();
    }
    private void loadData(){
        RecyclerView groupList = findViewById(R.id.select_group_budget_list);
        repository.fetchGroups((groups) ->{
            LinearLayout layoutContainer = findViewById(R.id.select_group_budget_list_layout);
            LinearLayout layoutEmpty = findViewById(R.id.select_group_budget_list_empty_layout);
            LinearLayout layoutLoading = findViewById(R.id.loading);
            List<Group> availableGroups = new ArrayList<Group>();

            for (Group gr :(List<Group>)groups) {
                layoutLoading.setVisibility(View.INVISIBLE);
                if(!activeGroups.contains(gr.id) && gr.type == false){
                    availableGroups.add(gr);
                }
            }
            if(availableGroups.size() >0){
                layoutContainer.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.INVISIBLE);
                MySelectGroupBudgetRvAdapter adapter = new MySelectGroupBudgetRvAdapter(SelectBudgetGroupActivity.this, availableGroups);
                groupList.setAdapter(adapter);
            }
            else {
                layoutEmpty.setVisibility(View.VISIBLE);
                layoutContainer.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initToolbar(){
        Toolbar toolbar = binding.basicAppBar;
        toolbar.setTitle("Nh??m giao d???ch");
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                SelectBudgetGroupActivity.this.finish();
                SelectBudgetGroupActivity.this.onBackPressed();
            }
        });
    }
}