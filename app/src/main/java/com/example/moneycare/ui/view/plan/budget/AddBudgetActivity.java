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

    BudgetRepository budgetRepository;
    NewBudgetViewModel viewModel;
    ActivityAddBudgetBinding binding;
    List<String> activeGroups = new ArrayList<String>();

    ActivityResultLauncher<Intent> toGroupActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Group group = data.getParcelableExtra("group");
                        viewModel.groupSelected.setValue(group);

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
        String[] tmp = intent.getStringArrayExtra("activeGroups");
        if(tmp != null){
            activeGroups = Arrays.asList(tmp);
        }
        init();

        viewModel = new ViewModelProvider(this).get(NewBudgetViewModel.class);
        binding = ActivityAddBudgetBinding.inflate(getLayoutInflater());
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();
        setContentView(binding.getRoot());

        initToolbar();
        initSelectGroupEvent();
        //Enter money
        initEnterMoney();
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
                intent.putExtra("activeGroups", activeGroups.toArray());
                toGroupActivityLauncher.launch(intent);
            }
        });
    }

    private void initEnterMoney(){
        EditText moneyEditText= findViewById(R.id.money_txt);
        moneyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable view) {
                if(convertToNumber(view.toString()) >= 0) {
                    moneyEditText.removeTextChangedListener(this);
                    String str = null;
                    try {
                        // The comma in the format specifier does the trick
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        str =  decimalFormat.format(convertToNumber(view.toString()));
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                    }
                    moneyEditText.setText(str);
                    viewModel.moneyLimit.setValue(convertToNumber(view.toString()));
                    moneyEditText.setSelection(moneyEditText.getText().length());
                    moneyEditText.addTextChangedListener(this);
                }else {
                    moneyEditText.setText("0");
                }
            }
        });
    }

    public Long convertToNumber(String str){
        try {
            String[] arrs = str.split(",");
            return Long.valueOf(String.join("", arrs));
        }
        catch(Exception e){
            return -1L;
        }
    }

    private void init(){
        budgetRepository = new BudgetRepository();
    }

    private void handleAdd(){
        if(viewModel.groupSelected.getValue() != null){
            Budget budget = new Budget();
            budget.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            budget.setBudget(viewModel.moneyLimit.getValue());
            String groupId;
            if(viewModel.groupSelected.getValue().isDefault == true){
                groupId = "/transaction-groups/" + viewModel.groupSelected.getValue().id;
            }
            else {
                groupId = "users/" + budgetRepository.userId + "/transaction-groups/" + viewModel.groupSelected.getValue().id;
            }
            budget.setGroup_id(groupId);
            budgetRepository.insertBudget(budget);
            AddBudgetActivity.this.setResult(Activity.RESULT_OK);
            AddBudgetActivity.this.finish();
        }

    }

}