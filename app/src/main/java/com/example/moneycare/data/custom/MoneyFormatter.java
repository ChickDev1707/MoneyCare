package com.example.moneycare.data.custom;

import com.example.moneycare.utils.appenum.MoneySeparator;

public class MoneyFormatter {
    // rule for money to save to settings
    public boolean isShortType;
    public boolean hasCurrencySymbol;
    public boolean hasFraction;
    public MoneySeparator separator;
    public MoneyFormatter() {
        isShortType = false;
        hasCurrencySymbol = false;
        hasFraction = false;
        separator = MoneySeparator.DOT;
    }
    public MoneyFormatter(boolean isShortType, boolean hasCurrencySymbol, boolean hasFraction, MoneySeparator separator){
        this.isShortType = isShortType;
        this.hasCurrencySymbol = hasCurrencySymbol;
        this.hasFraction = hasFraction;
        this.separator = separator;
    }

}
