package com.example.moneycare.ui.view.plan.budget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.BudgetRepository;
import com.example.moneycare.databinding.ActivityUpdateBudgetBinding;
import com.example.moneycare.ui.viewmodel.plan.BudgetViewModel;
import com.example.moneycare.utils.ImageLoader;

import java.text.DecimalFormat;

public class UpdateBudgetActivity extends AppCompatActivity {

    String idBudget;
    Long money;
    String imgGroup;
    String groupName;
    BudgetViewModel budgetsVM;
    BudgetRepository budgetRepository;
    ActivityUpdateBudgetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        budgetRepository = new BudgetRepository();

        Intent intent = getIntent();
        idBudget = intent.getStringExtra("idBudget");
        imgGroup = intent.getStringExtra("imgGroup");
        groupName = intent.getStringExtra("groupName");
        money = intent.getLongExtra("money",0L);


        budgetsVM = new ViewModelProvider(this).get(BudgetViewModel.class);
        binding = ActivityUpdateBudgetBinding.inflate(getLayoutInflater());
        binding.setBudgetVM(budgetsVM);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        loadDataWhenUpdate();
        initToolbar();
        initButtonUpdate();
    }

    private void initButtonUpdate(){
        Button btnUpdate= findViewById(R.id.btn_update_budget);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdate();
            }
        });
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle("Sửa ngân sách");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateBudgetActivity.this.finish();
            }
        });
    }
    private void loadDataWhenUpdate(){
        ImageView imgView = findViewById(R.id.update_budget_img_group);
        ImageLoader imageLoader = new ImageLoader(imgView);
        imageLoader.execute(imgGroup);

        Group group = new Group();
        group.name = groupName;
        budgetsVM.groupSelected.setValue(group);
        binding.updateBudgetGroupName.setEnabled(false);

        budgetsVM.moneyLimit.setValue(money);
    }

    private void handleUpdate(){
         budgetRepository.updateBudget(idBudget,  budgetsVM.moneyLimit.getValue());

        UpdateBudgetActivity.this.finish();
    }

}