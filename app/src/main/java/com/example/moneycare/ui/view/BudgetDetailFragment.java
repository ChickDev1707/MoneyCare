package com.example.moneycare.ui.view;

import static com.example.moneycare.utils.Convert.convertToNumber;
import static com.example.moneycare.utils.Convert.convertToThousandsSeparator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.repository.BudgetRepository;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.FragmentAddBudgetBinding;
import com.example.moneycare.databinding.FragmentBudgetDetailBinding;
import com.example.moneycare.ui.viewmodel.BudgetViewModel;
import com.example.moneycare.utils.LoadImage;

import java.text.DecimalFormat;
import java.time.ZoneId;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String idBudget;
    private String imgGroup;
    private String groupName;
    private BudgetRepository budgetRepository;
    private TransactionRepository transactionRepository;
    private BudgetViewModel budgetsVM;

    public BudgetDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudgetDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetDetailFragment newInstance(String param1, String param2) {
        BudgetDetailFragment fragment = new BudgetDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgGroup = getArguments().getString("imgGroup");
            idBudget = getArguments().getString("idBudget");
            groupName = getArguments().getString("groupName");
        }
        budgetRepository = new BudgetRepository();
        transactionRepository = new TransactionRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Handle binding with viewmodel
        budgetsVM = new ViewModelProvider(this).get(BudgetViewModel.class);
        FragmentBudgetDetailBinding binding = FragmentBudgetDetailBinding.inflate(inflater, container, false);
        binding.setBudgetVM(budgetsVM);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();

        //Load image
        LoadImage loadImage = new LoadImage(view.findViewById(R.id.img_item_detail));
        loadImage.execute(imgGroup);

        //Group name
        TextView tvGrName = view.findViewById(R.id.item_group_name_detail);
        tvGrName.setText(groupName);
        budgetRepository.fetchBudgetById(budget -> {
            budgetsVM.limitOfMonth.setValue(convertToThousandsSeparator(((Budget)budget).getBudgetOfMonth()));
            //Tổng đã chi
            budgetsVM.fetchTransactionsByGroup(((Budget)budget).getDate(),((Budget)budget).getGroup_id());
        }, idBudget);

        return view;
    }
}