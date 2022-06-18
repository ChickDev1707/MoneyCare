package com.example.moneycare.ui.view.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ReportFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.moneycare.R;
import com.example.moneycare.data.custom.GroupTransaction;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.GroupItemBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ReportTabLayoutAdapter extends FragmentStateAdapter {
    private List<GroupTransaction> groupTransactionList;
    private List<BarEntry>     dataChartNetIncome;
    private List<PieEntry> dataChartIncome;
    private List<PieEntry>     dataChartExpense;
    public ReportTabLayoutAdapter(@NonNull FragmentActivity fragmentActivity, List groupTransactionList, List dataChartNetIncome, List dataChartIncome, List dataChartExpense) {
        super(fragmentActivity);
        this.groupTransactionList = groupTransactionList;
        this.dataChartNetIncome = dataChartNetIncome;
        this.dataChartIncome = dataChartIncome;
        this.dataChartExpense = dataChartExpense;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentReportNetIncome(dataChartNetIncome);
            case 1:
                return new FragmentReportIncome(groupTransactionList, dataChartIncome);
            case 2:
                return new FragmentReportExpense(dataChartExpense);
            default:
                return new FragmentReportNetIncome(dataChartNetIncome);

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
