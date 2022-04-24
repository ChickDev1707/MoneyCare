package com.example.moneycare.ui.view.transaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.databinding.FragmentNewTransactionBinding;
import com.example.moneycare.databinding.FragmentUpdateTransactionBinding;
import com.example.moneycare.ui.viewmodel.transaction.NewTransactionViewModel;
import com.example.moneycare.ui.viewmodel.transaction.UpdateTransactionViewModel;
import com.example.moneycare.utils.appenum.TransactionTimeFrame;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateTransactionFragment extends Fragment {
    private UpdateTransactionViewModel updateTransViewModel;
    private FragmentUpdateTransactionBinding binding;
//    private Tran binding;

    public UpdateTransactionFragment() {
        // Required empty public constructor
    }

    public static UpdateTransactionFragment newInstance(String param1, String param2) {
        UpdateTransactionFragment fragment = new UpdateTransactionFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        updateTransViewModel =  new ViewModelProvider(this).get(UpdateTransactionViewModel.class);
        binding = FragmentUpdateTransactionBinding.inflate(getLayoutInflater());
        binding.setUpdateTransVM(updateTransViewModel);
        binding.setLifecycleOwner(this);

        Toolbar toolbar = binding.getRoot().findViewById(R.id.update_app_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        initSelectGroupEvent();
        observeSelectGroup();
        initUpdateTransactionBtn();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserTransaction transaction = TransactionFragmentArgs.fromBundle(getArguments()).getTransactionArg();
        updateTransViewModel.initTransaction(transaction);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.update_app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_item:
                updateTransViewModel.switchUpdateMode();
                return true;
            case R.id.delete_item:
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    private void observeSelectGroup(){
        NavController navController = NavHostFragment.findNavController(this);
        MutableLiveData<Bundle> liveData = navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .getLiveData("bundle");
        liveData.observe(getViewLifecycleOwner(), new Observer<Bundle>() {
            @Override
            public void onChanged(Bundle bundle) {
                Group group = (Group) bundle.getParcelable("group");
                updateTransViewModel.setGroup(group);
                System.out.println(group.name);
            }
        });
    }
    private void initSelectGroupEvent(){
        binding.updateTransGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.updateTransactionToGroupAction);
            }
        });
    }
    private void initUpdateTransactionBtn(){
        binding.updateTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTransViewModel.updateTransaction();
            }
        });
    }

}