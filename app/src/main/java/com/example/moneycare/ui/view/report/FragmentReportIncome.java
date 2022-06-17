package com.example.moneycare.ui.view.report;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class FragmentReportIncome extends Fragment {
    private PieChart       pieChartIncome;
    private List<PieEntry> dataChartIncome;
    public FragmentReportIncome(List data){
        this.dataChartIncome = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_income, container, false);
        pieChartIncome = view.findViewById(R.id.pieChartIncome1);
        initPieChartIncome();
        return view;
    }
    private void initPieChartIncome(){
        //setupPieChartIncome();
        pieChartIncome.setDrawHoleEnabled(true);
        pieChartIncome.setUsePercentValues(true);
        pieChartIncome.setEntryLabelTextSize(12);
        pieChartIncome.setEntryLabelColor(Color.BLACK);
//        pieChartIncome.setCenterText("Spending by Category");
//        pieChartIncome.setCenterTextSize(24);
        pieChartIncome.getDescription().setEnabled(false);

        Legend l = pieChartIncome.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        //loadPieChartIncomeData
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
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChartIncome.setData(data);
        pieChartIncome.invalidate();
        pieChartIncome.animateY(1400, Easing.EaseInOutQuad);
    }
}