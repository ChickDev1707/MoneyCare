package com.example.moneycare.ui.view.plan.budget;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.BudgetRepository;
import com.example.moneycare.databinding.ActivityAddBudgetBinding;
import com.example.moneycare.ui.viewmodel.plan.NewBudgetViewModel;
import com.example.moneycare.utils.DateTimeUtil;
import com.example.moneycare.utils.ImageLoader;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddBudgetActivity extends AppCompatActivity {

    NewBudgetViewModel viewModel;
    ActivityAddBudgetBinding binding;
    ArrayList<String> activeGroups = new ArrayList<String>();

    ActivityResultLauncher<Intent> toGroupActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Group group = data.getParcelableExtra("group");
                        viewModel.selectedGroup.setValue(group);

                        ImageView imgView = findViewById(R.id.img_item_select_group);
                        ImageLoader imageLoader = new ImageLoader(imgView);
                        imageLoader.execute(group.image);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        activeGroups = intent.getStringArrayListExtra("activeGroups");

        viewModel = new ViewModelProvider(this).get(NewBudgetViewModel.class);
        binding = ActivityAddBudgetBinding.inflate(getLayoutInflater());
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();
        setContentView(binding.getRoot());

        initToolbar();
        initSelectGroupEvent();
        initButtonAdd();

    }

    private void initButtonAdd (){
        Button btnAdd = findViewById(R.id.btn_add_budget);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAdd();
            }
        });
    }
    private void initToolbar(){
        //toolbar
        Toolbar toolbar = findViewById(R.id.basic_app_bar);

        toolbar.setTitle("Thêm ngân sách");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBudgetActivity.this.finish();
            }
        });
    }
    private void initSelectGroupEvent(){
        binding.addBudgetGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBudgetActivity.this, SelectBudgetGroupActivity.class);
                intent.putStringArrayListExtra("activeGroups", activeGroups);
                toGroupActivityLauncher.launch(intent);
            }
        });
    }

    private void handleAdd(){
        if(viewModel.selectedGroup.getValue() != null){
            viewModel.saveNewBudget();
            AddBudgetActivity.this.finish();
        }
    }

}