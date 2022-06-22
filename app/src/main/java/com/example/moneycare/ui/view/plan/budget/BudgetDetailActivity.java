package com.example.moneycare.ui.view.plan.budget;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.repository.BudgetRepository;
import com.example.moneycare.databinding.ActivityBudgetDetailBinding;
import com.example.moneycare.ui.viewmodel.plan.BudgetViewModel;
import com.example.moneycare.utils.ImageLoader;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class BudgetDetailActivity extends AppCompatActivity {
    private String idBudget;
    private String imgGroup;
    private String groupName;
    private Long totalBudget;
    private BudgetRepository budgetRepository;
    private BudgetViewModel budgetsVM;
    private ActivityBudgetDetailBinding binding;

    ActivityResultLauncher<Intent> toUpdateBudgetActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Long money = data.getLongExtra("money", 0L);

                        totalBudget = totalBudget - budgetsVM.limitOfMonth.getValue() + money;

                        budgetsVM.limitOfMonth.setValue(money);
                        budgetsVM.calculateSpendPerDay();
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        budgetRepository = new BudgetRepository();

        Intent intent = getIntent();
        imgGroup = intent.getStringExtra("imgGroup");
        groupName = intent.getStringExtra("groupName");
        idBudget = intent.getStringExtra("idBudget");
        totalBudget = intent.getLongExtra("totalBudget", 0L);

        //Handle binding with viewmodel
        budgetsVM = new ViewModelProvider(this).get(BudgetViewModel.class);
        binding = ActivityBudgetDetailBinding.inflate(getLayoutInflater());
        binding.setBudgetVM(budgetsVM);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        initToolbar();
        //Load image
        ImageLoader imageLoader = new ImageLoader(findViewById(R.id.img_item_detail));
        imageLoader.execute(imgGroup);

        loadData();


    }
    private void loadData(){
        //Group name
        TextView tvGrName = findViewById(R.id.item_group_name_detail);
        tvGrName.setText(groupName);
        budgetRepository.fetchBudgetById(budget -> {
            budgetsVM.limitOfMonth.setValue(((Budget)budget).getBudgetOfMonth());
            //Tổng đã chi
            budgetsVM.fetchTransactionsByGroup(((Budget)budget).getDate(),((Budget)budget).getGroup());
        }, idBudget);
    }
    private void initToolbar(){
        //toolbar
        Toolbar toolbar = findViewById(R.id.update_app_bar);
        toolbar.setTitle("Chi tiết ngân sách");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("totalBudget", totalBudget);
                BudgetDetailActivity.this.setResult(Activity.RESULT_OK, intent);
                BudgetDetailActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.update_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_item:
                Intent intent = new Intent(BudgetDetailActivity.this, UpdateBudgetActivity.class);
                intent.putExtra("idBudget", idBudget);
                intent.putExtra("imgGroup", imgGroup);
                intent.putExtra("groupName", groupName);
                intent.putExtra("money", budgetsVM.limitOfMonth.getValue());
                toUpdateBudgetActivity.launch(intent);
                return true;
            case R.id.delete_item:
                new MaterialAlertDialogBuilder(BudgetDetailActivity.this)
                        .setTitle("Cảnh báo").setMessage("Bạn có chắc muốn xóa ngân sách này hay không?")
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                budgetRepository.deleteBudget(idBudget);
                                BudgetDetailActivity.this.setResult(Activity.RESULT_OK);
                                BudgetDetailActivity.this.finish();
                            }
                        })
                        .show();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}