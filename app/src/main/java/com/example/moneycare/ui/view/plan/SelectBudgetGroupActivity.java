package com.example.moneycare.ui.view.plan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.TransactionGroupRepository;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.ActivitySelectBudgetGroupBinding;
import com.example.moneycare.ui.view.transaction.group.GroupListFragment;
import com.example.moneycare.ui.view.transaction.group.GroupMainFragment;
import com.example.moneycare.utils.Convert;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectBudgetGroupActivity extends AppCompatActivity {

    private ActivitySelectBudgetGroupBinding binding;
    private TransactionGroupRepository repository;
    private List<String> activeGroups = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectBudgetGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String[] tmp = intent.getStringArrayExtra("activeGroups");
        if(tmp != null){
            activeGroups = Arrays.asList(tmp);
        }
        repository = new TransactionGroupRepository();

        Toolbar toolbar = binding.basicAppBar;
        toolbar.setTitle("Nhóm giao dịch");
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                SelectBudgetGroupActivity.this.finish();
                SelectBudgetGroupActivity.this.onBackPressed();
            }
        });

        RecyclerView groupList = findViewById(R.id.select_group_budget_list);
         repository.fetchTransactionGroups((groups) ->{
             LinearLayout layoutContainer = findViewById(R.id.select_group_budget_list_layout);
             LinearLayout layoutEmpty = findViewById(R.id.select_group_budget_list_empty_layout);
             List<Group> availableGroups = new ArrayList<Group>();
             for (Group gr :(List<Group>)groups) {
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




}