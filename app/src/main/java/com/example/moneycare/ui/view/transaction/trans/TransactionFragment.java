package com.example.moneycare.ui.view.transaction.trans;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.databinding.FragmentTransactionListBinding;
import com.example.moneycare.ui.view.MainActivity;
import com.example.moneycare.ui.view.transaction.wallet.ModifyWalletActivity;
import com.example.moneycare.ui.viewmodel.transaction.TransactionViewModel;
import com.example.moneycare.utils.Converter;
import com.example.moneycare.utils.DateUtil;
import com.example.moneycare.utils.ImageUtil;
import com.example.moneycare.utils.appenum.TransactionTimeFrame;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;


/**
 * A fragment representing a list of Items.
 */

public class TransactionFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private TransactionViewModel viewModel;
    private FragmentTransactionListBinding binding;
    private TransactionTimeFrame timeFrameMode;
    private Date selectedDate;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initLayout();
        initToolbar();
        initTransactionSetting();
        initTransList();
        initOpenWalletListBtn();
        initWalletFromPreference();

        return binding.getRoot();
    }
    private void initLayout(){
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        binding = FragmentTransactionListBinding.inflate(getLayoutInflater());
        binding.setTransactionListVM(viewModel);
        binding.setLifecycleOwner(this);
        timeFrameMode = TransactionTimeFrame.DAY;
    }
    private void initTransList(){
        selectedDate = new Date();
        showTransList();
    }
    private void showTransList(){
        RecyclerView transList = binding.groupTransactionListTemplate;
        viewModel.setTransactionUI(getContext(), timeFrameMode, selectedDate , groupTransactionList -> {
            transList.setAdapter(new GroupTransactionRecyclerViewAdapter(groupTransactionList));
            viewModel.initMoneyInAndOut(groupTransactionList);
        });
    }
    private void initToolbar(){
        Toolbar toolbar = binding.getRoot().findViewById(R.id.top_app_bar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.date:
                    openPickDateDialog();
                return true;
            case R.id.time_frame_day:
                handleSelectTimeFrame(TransactionTimeFrame.DAY);
                return true;
            case R.id.time_frame_month:
                handleSelectTimeFrame(TransactionTimeFrame.MONTH);
                return true;
            case R.id.time_frame_year:
                handleSelectTimeFrame(TransactionTimeFrame.YEAR);
                return true;
            case R.id.modify_wallet:
                openModifyWallet();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    private void openModifyWallet(){
        MainActivity activity = (MainActivity) getActivity();
        Intent intent = new Intent(activity, ModifyWalletActivity.class);
        ActivityResultLauncher toModifyWalletLauncher = activity.getReloadTransFragmentLauncher();
        toModifyWalletLauncher.launch(intent);
    }
    private void handleSelectTimeFrame(TransactionTimeFrame timeFrame){
        timeFrameMode = timeFrame;
        saveTransactionSetting();
        initTransList();
    }
    private void openPickDateDialog(){
        switch (timeFrameMode){
            case DAY:
                showDatePicker();
                break;
            case MONTH:
                showMonthPicker();
                break;
            case YEAR:
                showYearPicker();
                break;
        }
    }
    private void showDatePicker(){
        Long today = MaterialDatePicker.todayInUtcMilliseconds();
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker datePicker = builder
                .setTitleText("Chọn khung thời gian")
                .setSelection(today)
                .build();
        datePicker.show(getParentFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                selectedDate = new Date((Long) selection);
                showTransList();
            }
        });
    }
    private void showMonthPicker(){
        Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                selectedDate = DateUtil.createDate(1, selectedMonth, selectedYear);
                showTransList();
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
        builder.setActivatedMonth(Calendar.JULY)
                .setMinYear(1990)
                .setActivatedYear(2022)
                .setMaxYear(2030)
                .setTitle("Select month")
                .build()
                .show();
    }
    private  void showYearPicker(){
        Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                selectedDate = DateUtil.createDate(1, selectedMonth, selectedYear);
                showTransList();
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
        builder.setActivatedMonth(Calendar.JULY)
                .setMinYear(1990)
                .setActivatedYear(2022)
                .setMaxYear(2030)
                .setTitle("Select year")
                .showYearOnly()
                .build()
                .show();
    }
    // setting
    private void saveTransactionSetting(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.app_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.pref_key_time_frame), timeFrameMode.getValue());
        editor.apply();
    }
    private void initTransactionSetting(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.app_preference), Context.MODE_PRIVATE);
        int timeFrameValue = sharedPref.getInt(getString(R.string.pref_key_time_frame), 1);
        timeFrameMode = TransactionTimeFrame.getTimeFrame(timeFrameValue);
    }
    // wallet
    private void initOpenWalletListBtn(){
        binding.mainAppBar.walletIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).launchReloadWallet();
            }
        });
    }
    private void initWalletFromPreference(){
        String walletId = getWalletFromPreference();
        if(walletId.equals("")){
            // no pref
            viewModel.fetchFirstWallet(wallet->{
                updateWalletUI(wallet);
                setWalletToPreference(wallet.id);
            });
        }else{
            viewModel.fetchWallet(walletId, this::updateWalletUI);
        }
    }
    private void updateWalletUI(Wallet wallet){
        binding.mainAppBar.walletName.setText(wallet.name);
        binding.mainAppBar.walletMoney.setText(Converter.toFormattedMoney(getContext(), wallet.money));

        if(!wallet.image.equals("")){
            Bitmap walletBimapImg = ImageUtil.toBitmap(wallet.image);
            BitmapDrawable walletBitmapDrawable = new BitmapDrawable(getResources(), walletBimapImg);
            binding.mainAppBar.walletIconBtn.setBackground(walletBitmapDrawable);
        }
    }
    private String getWalletFromPreference(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.app_preference), Context.MODE_PRIVATE);
        String walletId = sharedPref.getString(getString(R.string.pref_key_current_wallet), "");
        return walletId;
    }
    private void setWalletToPreference(String walletId){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.app_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_key_current_wallet), walletId);
        editor.apply();
    }
    public void handleSelectWallet(String walletId){
        setWalletToPreference(walletId);
        viewModel.fetchWallet(walletId, this::updateWalletUI);
    }
    public void initElements(){
        showTransList();
        initWalletFromPreference();
    }
}