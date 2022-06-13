package com.example.moneycare.ui.view.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.TooltipPositionMode;
import com.example.moneycare.R;
import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.databinding.FragmentReportBinding;
import com.example.moneycare.ui.view.MainActivity;
import com.example.moneycare.ui.viewmodel.report.ReportViewModel;
import com.example.moneycare.utils.DateUtil;
import com.example.moneycare.utils.ImageUtil;
import com.example.moneycare.utils.appenum.TransactionTimeFrame;
import com.example.moneycare.utils.enums.Position;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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
    private static final String                         ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private              ReportViewModel           viewModel;
    private              FragmentReportBinding binding;
    private              TransactionTimeFrame           timeFrameMode;
    private              Date                           selectedDate;
    private List<GroupTransaction> groupTransactionList;
    private Cartesian cartesian;
    private AnyChartView anyChartView;
    private Column column;


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


//        View view = inflater.inflate(R.layout.fragment_report, container, false);
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        binding = FragmentReportBinding.inflate(getLayoutInflater());
        binding.setReportListVM(viewModel);
        binding.setLifecycleOwner(this);
        groupTransactionList = null;
        timeFrameMode = TransactionTimeFrame.DAY;
        anyChartView = binding.getRoot().findViewById(R.id.charColumnNetIncome);
        cartesian = AnyChart.column();

        Toolbar toolbar = binding.getRoot().findViewById(R.id.top_app_bar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        initTransactionSetting();
        initTransList();
        initOpenWalletListBtn();
//        initWalletFromPreference();

//        initNetIncomeColumnChart();
//        initIncomePieChart();
//        initExpensePieChart();

        return binding.getRoot();
    }
    private List<DataEntry> chartNetIncomeDataProcessing(List<GroupTransaction> groupTransactionList){
        List<DataEntry> data = new ArrayList<>();

        long[] dataValues = new long[32];
        for (int i = 0; i<=31; i++){
            dataValues[i] = 0;
        }
        for (GroupTransaction groupTransaction: groupTransactionList){
            for (UserTransaction transaction:groupTransaction.transactionList){
                if (groupTransaction.group.type)
                    dataValues[DateUtil.getDay(transaction.date)]+=transaction.money/1000;
                else
                    dataValues[DateUtil.getDay(transaction.date)]-=transaction.money/1000;
            }
        }
        for (int i=1; i< dataValues.length; i++){
            data.add(new ValueDataEntry(i, dataValues[i]));
        }
//        this.groupTransactionList = groupTransactionList;
        return  data;
    }
    private List<DataEntry> dataChartPieIncomeProcessing(List<GroupTransaction> groupTransactionList){
        List<DataEntry> data = new ArrayList<>();
        for (GroupTransaction groupTransaction:groupTransactionList){
            data.add(new ValueDataEntry(groupTransaction.group.name, groupTransaction.getTotalMoney()));
        }
        return data;
    }
    private void initIncomePieChart(List<GroupTransaction> groupTransactionList){
        AnyChartView anyChartView = binding.getRoot().findViewById(R.id.charColumnNetIncome);

    }

    private void initNetIncomeColumnChart(List<GroupTransaction> groupTransactionList){
        List<DataEntry> data = chartNetIncomeDataProcessing(groupTransactionList);

        cartesian.data(data);

        cartesian.animation(true);
        cartesian.title("Biểu đồ Thu chi hằng ngày");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.yAxis(0).title("Nghìn VNĐ");
        cartesian.xAxis(0).title("Tháng");
        cartesian.labels(true);
        anyChartView.setChart(cartesian);
    }
    public void getDataChart(){
//        RecyclerView transList = binding.groupTransactionListTemplate;
        viewModel.getListTransReport(selectedDate , transactionList -> {
//            transList.setAdapter(new GroupTransactionRecyclerViewAdapter(groupTransactionList));
            for (int i=0; i<transactionList.size(); i++){

            }
        });
    }
    private void initTransList(){
        selectedDate = new Date();
        showTransList();
    }
    public void showTransList(){
//        RecyclerView transList = binding.groupTransactionListTemplate;
        viewModel.setUI(timeFrameMode, selectedDate , groupTransactionList -> {
//            transList.setAdapter(new GroupTransactionRecyclerViewAdapter(groupTransactionList));
            viewModel.initMoneyInAndOut(groupTransactionList);
//            chartDataProcessing(groupTransactionList);
            initNetIncomeColumnChart(groupTransactionList);
        });
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
                showMonthPicker();
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

    private void saveTransactionSetting(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.transaction_preference_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.time_frame_key), timeFrameMode.getValue());
        editor.apply();
    }
    public void initTransactionSetting(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.transaction_preference_key), Context.MODE_PRIVATE);
        int timeFrameValue = sharedPref.getInt(getString(R.string.time_frame_key), 1);
        timeFrameMode = TransactionTimeFrame.getTimeFrame(timeFrameValue);
    }
    private void initOpenWalletListBtn(){
        binding.mainAppBar.walletIconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent toSelectWalletIntent = new Intent(getActivity(), SelectWalletActivity.class);
//                startActivity(toSelectWalletIntent);
                ((MainActivity) getActivity()).launchReloadWallet();
            }
        });
    }
//    public void initWalletFromPreference(){
//        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.transaction_preference_key), Context.MODE_PRIVATE);
//        String walletId = sharedPref.getString(getString(R.string.current_wallet_key), "");
//
//        if(walletId == ""){
//            // no pref
//            viewModel.fetchFirstWallet(wallet->{
//                initWallet(wallet);
//                saveWalletPreference(wallet);
//            });
//        }else{
//            viewModel.fetchWallet(walletId, this::initWallet);
//        }
//    }
    private void initWallet(Wallet wallet){
        binding.mainAppBar.walletName.setText(wallet.name);
        binding.mainAppBar.walletMoney.setText(Long.toString(wallet.money));

        if(wallet.image!= ""){
            Bitmap walletBimapImg = ImageUtil.toBitmap(wallet.image);
            BitmapDrawable walletBitmapDrawable = new BitmapDrawable(getResources(), walletBimapImg);
            binding.mainAppBar.walletIconBtn.setBackground(walletBitmapDrawable);
        }
    }
    private void saveWalletPreference(Wallet wallet){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.transaction_preference_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.current_wallet_key), wallet.id);
        editor.apply();
    }

}