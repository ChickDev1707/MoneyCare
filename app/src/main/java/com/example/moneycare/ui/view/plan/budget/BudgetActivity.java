package com.example.moneycare.ui.view.plan.budget;

import static com.example.moneycare.utils.Convert.convertToMoneyCompact;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.ActivityBudgetBinding;
import com.example.moneycare.ui.viewmodel.plan.BudgetViewModel;
import com.example.moneycare.utils.Convert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private BudgetViewModel budgetVM;
    private RecyclerView budgetGroupList;
    private ActivityBudgetBinding binding;

    ActivityResultLauncher<Intent> toDetailBudgetActivity = registerForActivityResult(
             new ActivityResultContracts.StartActivityForResult(),
                     new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Intent data = result.getData();
                if(data != null){
                    Long totalBudget = data.getLongExtra("totalBudget", 0L);
                    budgetVM.totalBudgetImpl = totalBudget;
                    budgetVM.totalBudget.setValue(convertToMoneyCompact(totalBudget));
                    budgetVM.spendableMoneyImpl = budgetVM.totalBudgetImpl -  budgetVM.totalSpentImpl;
                    budgetVM.spendableMoney.setValue(convertToMoneyCompact(budgetVM.spendableMoneyImpl));
                }
                else {
                    finish();
                    startActivity(getIntent());
                }
            }
        }
    });

    ActivityResultLauncher<Intent> toAddBudgetActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                       finish();
                       startActivity(getIntent());
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        budgetVM = new ViewModelProvider(this).get(BudgetViewModel.class);
        binding = ActivityBudgetBinding.inflate(getLayoutInflater());
        binding.setBudgetVM(budgetVM);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.add_tool_bar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudgetActivity.this.finish();
            }
        });

        TextView txtAdd = findViewById(R.id.btn_add);
        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(BudgetActivity.this, AddBudgetActivity.class);
                List<String> arrGroups = new ArrayList<String>();
                if(budgetVM.activeGroups != null){
                    for(Group gr:budgetVM.activeGroups){
                        arrGroups.add(gr.id);
                    }
                    intent.putExtra("activeGroups", arrGroups.toArray(new String[0]));
                }
                toAddBudgetActivity.launch(intent);
            }
        });

        budgetGroupList = findViewById(R.id.budget_gr_list);

        MyBudgetGroupRecyclerViewAdapter adapter = new MyBudgetGroupRecyclerViewAdapter();
        adapter.toDetailBudgetActivity = toDetailBudgetActivity;
        adapter.budgetsVM = budgetVM;
        budgetVM.fetchTransactionGroupsByBudget((groups, budgets) ->{
            LinearLayout layoutContainer = findViewById(R.id.budget_container);
            LinearLayout layoutEmpty = findViewById(R.id.budget_empty);
            LinearLayout layoutLoading = findViewById(R.id.loading);
            layoutLoading.setVisibility(View.INVISIBLE);

            if(groups.size() > 0){
                layoutContainer.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.INVISIBLE);
                adapter.setBudgets(budgets);
                adapter.setTransactionGroups(groups);

                budgetGroupList.setAdapter(adapter);
                if(budgets.size() > 0){
                    budgetVM.daysLeft.setValue(LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth() + 1);
                }
                final Long[] sum = {0L};
                ((List<Budget>)budgets).forEach(n-> {
                    sum[0] = sum[0] + n.getBudgetOfMonth();
                });
                budgetVM.activeGroups = groups;
                budgetVM.totalBudgetImpl = sum[0];
                budgetVM.totalBudget.setValue(Convert.convertToMoneyCompact(sum[0]));
                budgetVM.getTotalSpentInMonth();
            }
            else if(groups.size() == 0){
                layoutEmpty.setVisibility(View.VISIBLE);
                layoutContainer.setVisibility(View.INVISIBLE);
            }
        });

    }
}