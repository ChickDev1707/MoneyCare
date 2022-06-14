package com.example.moneycare.ui.view.plan.budget;

import static com.example.moneycare.utils.Convert.convertToNumber;

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
import com.example.moneycare.utils.LoadImage;

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

        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle("Sửa ngân sách");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateBudgetActivity.this.finish();
            }
        });

        Button btnUpdate= findViewById(R.id.btn_update_budget);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdate();
            }
        });

        initEnterMoney();

    }

    private void loadDataWhenUpdate(){
        ImageView imgView = findViewById(R.id.update_budget_img_group);
        LoadImage loadImage = new LoadImage(imgView);
        loadImage.execute(imgGroup);
        imgView.setBackgroundColor(0xFFFFFF);

        Group group = new Group();
        group.name = groupName;
        budgetsVM.groupSelected.setValue(group);
        binding.updateBudgetGroupName.setEnabled(false);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        EditText moneyEditText = findViewById(R.id.update_money_txt);
        moneyEditText.setText(decimalFormat.format(money));
    }

    private void handleUpdate(){
         budgetRepository.updateBudget(idBudget,  budgetsVM.moneyLimit.getValue());
        Intent intent = new Intent();
        intent.putExtra("money", budgetsVM.moneyLimit.getValue());
        UpdateBudgetActivity.this.setResult(Activity.RESULT_OK, intent);
        UpdateBudgetActivity.this.finish();
    }

    private void initEnterMoney(){
        EditText moneyEditText= findViewById(R.id.update_money_txt);
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
                    budgetsVM.moneyLimit.setValue(convertToNumber(view.toString()));
                    moneyEditText.setSelection(moneyEditText.getText().length());
                    moneyEditText.addTextChangedListener(this);
                }else {
                    moneyEditText.setText("0");
                }
            }
        });
    }
}