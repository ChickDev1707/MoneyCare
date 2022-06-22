package com.example.moneycare.ui.view.report.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.databinding.FragmentReportExpenseBinding;
import com.example.moneycare.ui.view.report.adapter.ReportExpenseRecyclerViewAdapter;
import com.example.moneycare.utils.Converter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class FragmentReportExpense extends Fragment {
    private FragmentReportExpenseBinding binding;
    private List<GroupTransaction> groupTransactionExpenseList;
    private     PieChart       pieChartExpense;
    private List<PieEntry> dataChartExpense;
    private long totalMoneyExpense;
    public FragmentReportExpense(List<GroupTransaction> groupTransactionList, long totalMoneyExpense){
        this.totalMoneyExpense = totalMoneyExpense;
        this.groupTransactionExpenseList = new ArrayList<>();
        this.dataChartExpense = new ArrayList<>();
        long otherValue = 0;
        for (GroupTransaction groupTransaction:groupTransactionList){
            if (!groupTransaction.group.type){
                this.groupTransactionExpenseList.add(groupTransaction);
                if (groupTransaction.getTotalMoney()*1.0f/this.totalMoneyExpense >= 0.03f){
                    this.dataChartExpense.add(new PieEntry(groupTransaction.getTotalMoney(), groupTransaction.group.name));
                }
                else {
                    otherValue+=groupTransaction.getTotalMoney();
                }
            }
        }
        if (otherValue>0){
            this.dataChartExpense.add(new PieEntry(otherValue, "..."));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_report_expense, container, false);
        binding = FragmentReportExpenseBinding.inflate(getLayoutInflater());
        pieChartExpense = binding.pieChartExpense;
        initPieChartExpense();
        loadRVGroupTransIncome();
        return binding.getRoot();
    }
    private void setupPieChartExpense(){
        pieChartExpense.setDrawHoleEnabled(true);
        pieChartExpense.setUsePercentValues(true);
        pieChartExpense.setEntryLabelTextSize(14);
        pieChartExpense.setEntryLabelColor(Color.BLACK);
        pieChartExpense.setCenterText("Tổng số: " + Converter.toFormattedMoney(getContext(),this.totalMoneyExpense));
        pieChartExpense.setCenterTextSize(22);
        pieChartExpense.getDescription().setEnabled(false);

        Legend l = pieChartExpense.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }
    private void loadPieChartExpenseData() {
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(dataChartExpense, "");
        dataSet.setColors(colors);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setSelectionShift(20f);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChartExpense));
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);

        pieChartExpense.setData(data);
        pieChartExpense.invalidate();
        pieChartExpense.animateY(1400, Easing.EaseInOutQuad);
    }
    private void initPieChartExpense(){
        setupPieChartExpense();
        loadPieChartExpenseData();
    }
    private void loadRVGroupTransIncome(){
        RecyclerView transList = binding.reportListTransactionExpense;
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        transList.setLayoutManager(manager);
        transList.setHasFixedSize(true);
        transList.setAdapter(new ReportExpenseRecyclerViewAdapter(groupTransactionExpenseList));
    }

}