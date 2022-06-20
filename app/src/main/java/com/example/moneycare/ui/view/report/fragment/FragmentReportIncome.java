package com.example.moneycare.ui.view.report.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.databinding.FragmentReportIncomeBinding;
import com.example.moneycare.ui.view.report.adapter.ReportIncomeRecyclerViewAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FragmentReportIncome extends Fragment {
    private FragmentReportIncomeBinding binding;
    private List<GroupTransaction>      groupTransactionIncomeList;
    private PieChart       pieChartIncome;
    private List<PieEntry> dataChartIncome;
    private long totalMoneyIncome;
    public FragmentReportIncome(List<GroupTransaction> groupTransactionList, List data, long totalMoneyIncome){
        this.groupTransactionIncomeList = new ArrayList<>();
        for (GroupTransaction groupTransaction:groupTransactionList){
            if (groupTransaction.group.type){
                this.groupTransactionIncomeList.add(groupTransaction);
            }
        }
//        this.groupTransactionIncomeList = sortTotalMoneyOfGroupTrans(this.groupTransactionIncomeList);
        this.dataChartIncome = data;
        this.totalMoneyIncome = totalMoneyIncome;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_report_income, container, false);
        binding = FragmentReportIncomeBinding.inflate(getLayoutInflater());
        pieChartIncome = binding.pieChartIncome;
        initPieChartIncome();
        loadRVGroupTransExpense();
        return binding.getRoot();
    }

    private void loadRVGroupTransExpense() {
        RecyclerView transList = binding.reportListTransactionIncome;
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        transList.setLayoutManager(manager);
        transList.setHasFixedSize(true);
        transList.setAdapter(new ReportIncomeRecyclerViewAdapter(groupTransactionIncomeList));
    }

    private void initPieChartIncome(){
        setupPieChartIncome();
        loadPieChartIncomeData();
    }

    private void loadPieChartIncomeData() {
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(dataChartIncome, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChartIncome));
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);

        pieChartIncome.setData(data);
        pieChartIncome.invalidate();
        pieChartIncome.animateY(1400, Easing.EaseInOutQuad);
    }

    private void setupPieChartIncome() {
        pieChartIncome.setDrawHoleEnabled(true);
        pieChartIncome.setUsePercentValues(true);
        pieChartIncome.setEntryLabelTextSize(14);
        pieChartIncome.setEntryLabelColor(Color.BLACK);
        pieChartIncome.setCenterText("Tổng số: " + Long.toString(this.totalMoneyIncome));
        pieChartIncome.setCenterTextSize(22);
        pieChartIncome.getDescription().setEnabled(false);

        Legend l = pieChartIncome.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }
}