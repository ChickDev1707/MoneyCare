package com.example.moneycare.ui.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.databinding.FragmentBudgetBinding;
import com.example.moneycare.ui.viewmodel.BudgetViewModel;
import com.example.moneycare.R;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

public class BudgetFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private BudgetViewModel viewModel;
    private RecyclerView budgetGroupList;

    public static BudgetFragment newInstance() {
        return new BudgetFragment();
    }

    public BudgetFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BudgetFragment newInstance(int columnCount) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        FragmentBudgetBinding binding = FragmentBudgetBinding.inflate(inflater, container, false);
        binding.setBudgetVM(viewModel);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();

        budgetGroupList = view.findViewById(R.id.budget_gr_list);

        // Set the adapter

        Context context = budgetGroupList.getContext();
        if (mColumnCount <= 1) {
            budgetGroupList.setLayoutManager(new LinearLayoutManager(context));
        } else {
            budgetGroupList.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        viewModel.fetchTransactionGroups(groups -> budgetGroupList.setAdapter(new MyBudgetGroupRecyclerViewAdapter(groups)));
        viewModel.fetchInMonth((budgets) -> {
            if(budgets.size() > 0){
                viewModel.daysLeft.setValue(LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth() + 1);
            }
            final Long[] sum = {0L};
            budgets.forEach(n-> {
                sum[0] = sum[0] + n.getBudget();
            });
            viewModel.totalBudget.setValue(sum[0]);
        });
        return view;
    }
}