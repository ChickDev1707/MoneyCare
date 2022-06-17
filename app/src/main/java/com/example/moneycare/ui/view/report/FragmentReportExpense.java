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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class FragmentReportExpense extends Fragment {
    private     PieChart       pieChartExpense;
    private List<PieEntry> dataChartExpense;
    public FragmentReportExpense(List data){
        this.dataChartExpense = data;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_expense, container, false);
        pieChartExpense = view.findViewById(R.id.pieChartExpense1);
        initPieChartExpense();
        return view;
    }
    private void initPieChartExpense(){
        //setupPieChartExpense
        pieChartExpense.setDrawHoleEnabled(true);
        pieChartExpense.setUsePercentValues(true);
        pieChartExpense.setEntryLabelTextSize(12);
        pieChartExpense.setEntryLabelColor(Color.BLACK);
//        pieChartExpense.setCenterText("Spending by Category");
//        pieChartExpense.setCenterTextSize(24);
        pieChartExpense.getDescription().setEnabled(false);

        Legend l = pieChartExpense.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        //loadPieChartExpenseData
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(dataChartExpense, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChartExpense));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChartExpense.setData(data);
        pieChartExpense.invalidate();
        pieChartExpense.animateY(1400, Easing.EaseInOutQuad);
    }
}