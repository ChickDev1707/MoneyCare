package com.example.moneycare.ui.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Transaction;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.FragmentLoginBinding;
import com.example.moneycare.databinding.FragmentTransactionBinding;
import com.example.moneycare.ui.viewmodel.TransactionViewModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TransactionFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private TransactionViewModel viewModel;
    Button newTransBtn;
    RecyclerView transList;
//    TransactionRepository repository;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TransactionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TransactionFragment newInstance(int columnCount) {
        TransactionFragment fragment = new TransactionFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        newTransBtn = (Button) view.findViewById(R.id.newTransBtn);
        transList = view.findViewById(R.id.transList);

        newTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_transactionFragment_to_newTransaction);
            }
        });
//        repository = new TransactionRepository();
        // Set the adapter

        Context context = transList.getContext();
        if (mColumnCount <= 1) {
            transList.setLayoutManager(new LinearLayoutManager(context));
        } else {
            transList.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        viewModel.fetchTransactions(transactions -> transList.setAdapter(new MyTransactionRecyclerViewAdapter(transactions)));
        return view;
    }
}