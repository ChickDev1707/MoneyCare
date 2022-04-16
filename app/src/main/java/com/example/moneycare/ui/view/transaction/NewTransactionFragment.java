package com.example.moneycare.ui.view.transaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.FragmentNewTransactionBinding;
import com.example.moneycare.ui.viewmodel.transaction.NewTransactionViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTransactionFragment extends Fragment {

    private NewTransactionViewModel newTransViewModel;
    private FragmentNewTransactionBinding binding;
    private List<Group> groups;

    public NewTransactionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewTransactionFragment newInstance(String param1, String param2) {
        NewTransactionFragment fragment = new NewTransactionFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // View view = inflater.inflate(R.layout.fragment_new_transaction, container, false);
        newTransViewModel = new ViewModelProvider(this).get(NewTransactionViewModel.class);
        binding = FragmentNewTransactionBinding.inflate(getLayoutInflater());
        binding.setNewTransVM(newTransViewModel);
        binding.setLifecycleOwner(this);
        // Set up binding

        initPickDateInput();
        initSelectGroupEvent();
        initSaveTransBtn();
        observeSelectGroup();

        // Inflate the layout for this fragment
        return binding.getRoot();
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
                newTransViewModel.setGroup(group);
            }
        });
    }
    private void initPickDateInput(){
//        binding.newTransDate.setEnabled(false);
        binding.newTransDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long today = MaterialDatePicker.todayInUtcMilliseconds();
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                MaterialDatePicker datePicker = builder
                        .setTitleText("Chọn ngày giao dịch")
                        .setSelection(today)
                        .build();
                datePicker.show(getParentFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        newTransViewModel.setDate(selection);
                    }
                });
            }
        });
    }
    private void initSaveTransBtn(){
        binding.saveNewTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTransViewModel.saveNewTransaction();
                Navigation.findNavController(view).popBackStack();
            }
        });
    }
    private void initSelectGroupEvent(){
        binding.newTransGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_newTransactionFragment_to_groupFragment);
            }
        });
    }
}