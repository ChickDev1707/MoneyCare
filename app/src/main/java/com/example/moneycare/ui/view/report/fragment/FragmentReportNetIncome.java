package com.example.moneycare.ui.view.report.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class FragmentReportNetIncome extends Fragment {
    private BarChart barChartNetIncome;
    private     List<BarEntry> dataChartNetIncome;
    public FragmentReportNetIncome(List data){
        this.dataChartNetIncome = data;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_net_income, container, false);
        barChartNetIncome = view.findViewById(R.id.barChartNetIncome1);
        initBarChartNetIncome();
        return view;
    }
    private void initBarChartNetIncome(){
        BarDataSet barDataSet = new BarDataSet(dataChartNetIncome,"");
        barDataSet.setDrawIcons(false);

        barChartNetIncome.setDrawBarShadow(false);
        barChartNetIncome.setDrawValueAboveBar(true);

        barChartNetIncome.getDescription().setEnabled(false);
        barChartNetIncome.setPinchZoom(false);
        barChartNetIncome.setDrawGridBackground(false);

        XAxis xAxis = barChartNetIncome.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barChartNetIncome.setData(barData);
        barChartNetIncome.animateY(1400);
        barChartNetIncome.invalidate();
    }
}