package com.example.moneycare.ui.view.report.fragment;

import static com.example.moneycare.utils.appenum.TransactionTimeFrame.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.R;
import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.databinding.FragmentReportBinding;
import com.example.moneycare.ui.view.MainActivity;
import com.example.moneycare.ui.view.report.adapter.ReportTabLayoutAdapter;
import com.example.moneycare.ui.view.transaction.wallet.SelectWalletActivity;
import com.example.moneycare.ui.viewmodel.report.ReportViewModel;
import com.example.moneycare.utils.Converter;
import com.example.moneycare.utils.DateTimeUtil;
import com.example.moneycare.utils.ImageUtil;
import com.example.moneycare.utils.appenum.TransactionTimeFrame;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment{

    // TODO: Customize parameter argument names
    private static final String          ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private ReportViewModel       viewModel;
    private FragmentReportBinding binding;
    private TransactionTimeFrame  timeFrameMode;
    private              Date                           selectedDate;
    private TabLayout tabLayout;
    private ViewPager2             viewPager2;
    private ReportTabLayoutAdapter reportAdapter;
    private List<GroupTransaction> groupTransactionList;
    private List<BarEntry> dataChartNetIncome;
//    private List<PieEntry>    dataChartIncome;
//    private List<PieEntry> dataChartExpense;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReportFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ReportFragment newInstance(int columnCount) {
        ReportFragment fragment = new ReportFragment();
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
    public void initLayout(){
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        binding = FragmentReportBinding.inflate(getLayoutInflater());
        binding.setReportListVM(viewModel);
        binding.setLifecycleOwner(this);
        timeFrameMode = DAY;
        groupTransactionList = new ArrayList<>();
        dataChartNetIncome = new ArrayList<>();
    }
    public void initToolbar(){
        Toolbar toolbar = binding.getRoot().findViewById(R.id.main_app_bar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }
    private void initTransList(){
        selectedDate = new Date();
        showTransList();
    }
    public void showTransList(){
        viewModel.setUI(getContext(), timeFrameMode, selectedDate , groupTransactionList -> {
            viewModel.initMoneyInAndOut(groupTransactionList);
            dataProcessingChart(groupTransactionList);
            initViewPager();
        });
    }
    private void dataProcessingChart(List<GroupTransaction> groupTransactionList){
        this.groupTransactionList.clear();
        this.groupTransactionList = sortTotalMoneyOfGroupTrans(groupTransactionList);
        dataChartNetIncome.clear();
        switch (timeFrameMode) {
            case DAY:
                dataProcessingChartInDay(this.groupTransactionList);
                break;
            case MONTH:
                dataProcessingChartInMonth(this.groupTransactionList);
                break;
            case YEAR:
                dataProcessingChartInYear(this.groupTransactionList);
                break;
            default:
                break;
        }
    }
    public List<GroupTransaction> sortTotalMoneyOfGroupTrans(List<GroupTransaction> groupTransactionList){
        List<GroupTransaction> groupTransactions = new ArrayList<>();
        if (groupTransactionList.size()>0){
            groupTransactions.add(groupTransactionList.get(0));
            for (int i=1; i<groupTransactionList.size(); i++){
                boolean iOK = true;
                for (int j=0; j<groupTransactions.size();j++){
                    if (groupTransactions.get(j).getTotalMoney()<=groupTransactionList.get(i).getTotalMoney()){
                        groupTransactions.add(j, groupTransactionList.get(i));
                        iOK = false;
                        break;
                    }
                }
                if (iOK) groupTransactions.add(groupTransactionList.get(i));
            }
        }
        return groupTransactions;
    }
    private void dataProcessingChartInDay(List<GroupTransaction> groupTransactionList){
        long[] dataValues = new long[24];
        for (int i = 0; i<=23; i++){
            dataValues[i] = 0;
        }
        if (timeFrameMode == DAY){
            for (GroupTransaction groupTransaction: groupTransactionList){
//                if(groupTransaction.group.type){
//                    dataChartIncome.add(new PieEntry(groupTransaction.getTotalMoney(), groupTransaction.group.name));
//                }
//                else{
//                    dataChartExpense.add(new PieEntry(groupTransaction.getTotalMoney(), groupTransaction.group.name));
//                }
                for (UserTransaction transaction:groupTransaction.transactionList){
                    if (groupTransaction.group.type)
                        dataValues[DateTimeUtil.getHour(transaction.date)]+=transaction.money/1000;
                    else
                        dataValues[DateTimeUtil.getHour(transaction.date)]-=transaction.money/1000;
                }
            }
            for (int i=0; i<= 23; i++){
                dataChartNetIncome.add(new BarEntry(i, dataValues[i]));
            }
        }
    }
    private void dataProcessingChartInMonth(List<GroupTransaction> groupTransactionList){
        long[] dataValues = new long[32];
        for (int i = 0; i<=31; i++){
            dataValues[i] = 0;
        }
        for (GroupTransaction groupTransaction: groupTransactionList){
            for (UserTransaction transaction:groupTransaction.transactionList){
                if (groupTransaction.group.type)
                    dataValues[DateTimeUtil.getDay(transaction.date)]+=transaction.money/1000;
                else
                    dataValues[DateTimeUtil.getDay(transaction.date)]-=transaction.money/1000;
            }
//            if(groupTransaction.group.type){
//                dataChartIncome.add(new PieEntry(groupTransaction.getTotalMoney(), groupTransaction.group.name));
//            }
//            else{
//                dataChartExpense.add(new PieEntry(groupTransaction.getTotalMoney(), groupTransaction.group.name));
//            }
        }
        for (int i=1; i<= 31; i++){
            dataChartNetIncome.add(new BarEntry(i, dataValues[i]));
        }
    }
    private void dataProcessingChartInYear(List<GroupTransaction> groupTransactionList){
        long[] dataValues = new long[13];
        for (int i = 0; i<=12; i++){
            dataValues[i] = 0;
        }
        for (GroupTransaction groupTransaction: groupTransactionList){
            for (UserTransaction transaction:groupTransaction.transactionList){
                if (groupTransaction.group.type)
                    dataValues[DateTimeUtil.getMonth(transaction.date)]+=transaction.money/1000;
                else
                    dataValues[DateTimeUtil.getMonth(transaction.date)]-=transaction.money/1000;
            }
//            if(groupTransaction.group.type){
//                dataChartIncome.add(new PieEntry(groupTransaction.getTotalMoney(), groupTransaction.group.name));
//            }
//            else{
//                dataChartExpense.add(new PieEntry(groupTransaction.getTotalMoney(), groupTransaction.group.name));
//            }
        }
        for (int i=1; i<= 12; i++){
            dataChartNetIncome.add(new BarEntry(i, dataValues[i]));
        }
    }
    private void initViewPager(){
        tabLayout = binding.getRoot().findViewById(R.id.tabLayout);
        viewPager2 = binding.getRoot().findViewById(R.id.viewPager2);
        reportAdapter = new ReportTabLayoutAdapter(this.getActivity(), groupTransactionList, dataChartNetIncome, viewModel.moneyIn.getValue(), viewModel.moneyOut.getValue(), viewModel.moneyTotal.getValue());
        viewPager2.setAdapter(reportAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Thu nhập ròng");
                    break;
                case 1:
                    tab.setText("Thu nhập");
                    break;
                case 2:
                    tab.setText("Chi tiêu");
                    break;
            }
        }).attach();
    }



    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_app_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.date:
                openPickDateDialog();
                return true;
            case R.id.time_frame_day:
                handleSelectTimeFrame(DAY);
                return true;
            case R.id.time_frame_month:
                handleSelectTimeFrame(MONTH);
                return true;
            case R.id.time_frame_year:
                handleSelectTimeFrame(YEAR);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
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
                selectedDate = DateTimeUtil.createDate(1, selectedMonth, selectedYear);
                showTransList();
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
        builder.setActivatedMonth(Calendar.JUNE)
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
                selectedDate = DateTimeUtil.createDate(1, selectedMonth, selectedYear);
                showTransList();
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
        builder.setActivatedMonth(Calendar.JUNE)
                .setMinYear(1990)
                .setActivatedYear(2022)
                .setMaxYear(2030)
                .setTitle("Select year")
                .showYearOnly()
                .build()
                .show();
    }

    private void saveTransactionSetting(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.transaction_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.pref_key_time_frame), timeFrameMode.getValue());
        editor.apply();
    }
    private void initTransactionSetting(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.transaction_preference), Context.MODE_PRIVATE);
        int timeFrameValue = sharedPref.getInt(getString(R.string.pref_key_time_frame), 1);
        timeFrameMode = TransactionTimeFrame.getTimeFrame(timeFrameValue);
    }
    private void initOpenWalletListBtn(){
        binding.appBarLayout.walletIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectWalletActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initWalletFromPreference(){
        String walletId = getWalletFromPreference();
        if(walletId.equals("")){
            // no pref
            initWalletForEmptyPref();
        }else{
            viewModel.walletRepository.fetchWallet(walletId, this::updateWalletUI);
        }
    }
    private void initWalletForEmptyPref(){
        viewModel.walletRepository.countWallets(noWallets->{
            if(noWallets> 0){
                viewModel.walletRepository.fetchFirstWallet(firstWallet->{
                    updateWalletUI(firstWallet);
                    setWalletToPreference(firstWallet.id);
                });
            }else updateWalletUI(new Wallet(null, "Tên ví", 0, ""));
        });
    }
    private void updateWalletUI(Wallet wallet){
        binding.appBarLayout.walletName.setText(wallet.name);
        binding.appBarLayout.walletMoney.setText(Converter.toFormattedMoney(getContext(), wallet.money));

        if(!wallet.image.equals("")){
            Bitmap walletBimapImg = ImageUtil.toBitmap(wallet.image);
            binding.appBarLayout.walletIconBtn.setImageBitmap(walletBimapImg);
        }
    }
    private String getWalletFromPreference(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.transaction_preference), Context.MODE_PRIVATE);
        String walletId = sharedPref.getString(getString(R.string.pref_key_current_wallet), "");
        return walletId;
    }
    private void setWalletToPreference(String walletId){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.transaction_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_key_current_wallet), walletId);
        editor.apply();
    }
    public void initElements(){
        showTransList();
        initWalletFromPreference();
    }

    @Override
    public void onResume() {
        super.onResume();
        initElements();
    }
}