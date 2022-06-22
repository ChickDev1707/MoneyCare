package com.example.moneycare.ui.view.report.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.ui.view.report.fragment.FragmentReportExpense;
import com.example.moneycare.ui.view.report.fragment.FragmentReportIncome;
import com.example.moneycare.ui.view.report.fragment.FragmentReportNetIncome;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

public class ReportTabLayoutAdapter extends FragmentStateAdapter {
    private List<GroupTransaction> groupTransactionList;
    private List<BarEntry>     dataChartNetIncome;
//    private List<PieEntry> dataChartIncome;
//    private List<PieEntry>     dataChartExpense;
    private long totalMoneyIncome;
    private long totalMoneyExpense;
    private long totalMoneyNetIncome;

    public ReportTabLayoutAdapter(@NonNull FragmentActivity fragmentActivity, List groupTransactionList, List dataChartNetIncome, long totalMoneyIncome, long totalMoneyExpense, long totalMoneyNetIncome) {
        super(fragmentActivity);
        this.groupTransactionList = groupTransactionList;
        this.dataChartNetIncome = dataChartNetIncome;
//        this.dataChartIncome = dataChartIncome;
//        this.dataChartExpense = dataChartExpense;
        this.totalMoneyIncome = totalMoneyIncome;
        this.totalMoneyExpense = totalMoneyExpense;
        this.totalMoneyNetIncome = totalMoneyNetIncome;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new FragmentReportIncome(groupTransactionList, totalMoneyIncome);
            case 2:
                return new FragmentReportExpense(groupTransactionList, totalMoneyExpense);
            default:
                return new FragmentReportNetIncome(dataChartNetIncome, totalMoneyNetIncome);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
