package com.example.moneycare.ui.view;

import static com.example.moneycare.utils.Convert.convertToNumber;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.TransactionGroup;
import com.example.moneycare.data.repository.TransactionGroupRepository;
import com.example.moneycare.databinding.FragmentAddBudgetBinding;
import com.example.moneycare.databinding.FragmentBudgetBinding;
import com.example.moneycare.ui.viewmodel.BudgetViewModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBudgetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<TransactionGroup> items;
    TransactionGroupRepository transactionGroupRepository;
    BudgetViewModel budgetsVM;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddBudgetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBudgetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBudgetFragment newInstance(String param1, String param2) {
        AddBudgetFragment fragment = new AddBudgetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionGroupRepository = new TransactionGroupRepository();
        items = new ArrayList<TransactionGroup>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Handle binding with viewmodel
        budgetsVM = new ViewModelProvider(this).get(BudgetViewModel.class);
        FragmentAddBudgetBinding binding = FragmentAddBudgetBinding.inflate(inflater, container, false);
        binding.setBudgetVM(budgetsVM);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();

        AutoCompleteTextView acTextView = view.findViewById(R.id.autoComplete);
        transactionGroupRepository.fetchTransactionGroups(groups -> {
            for(TransactionGroup group:groups){
                items.add(group);
            }
        });
        ArrayAdapter<TransactionGroup> adapter = new ArrayAdapter<TransactionGroup>(inflater.getContext(),R.layout.fragment_autocomplete_group_item,items);
        acTextView.setAdapter(adapter);
        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransactionGroup selectedGroup =(TransactionGroup) parent.getAdapter().getItem(position);
                budgetsVM.groupSelected.setValue(selectedGroup);
            }
        });
        EditText moneyEditText= view.findViewById(R.id.money_txt);
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
        return view;
    }


}