package com.example.moneycare.utils;

import android.content.Context;

import androidx.databinding.InverseMethod;

import com.example.moneycare.data.custom.AppMoney;
import com.example.moneycare.data.custom.MoneyFormatter;

public class Converter {

    @InverseMethod("stringToLong")
    public static String longToString(Long number) {
        if(number == 0L) return "";
        return number.toString();
    }

    public static Long stringToLong(String numString) {
        if(numString.equals("")) return 0L;
        else return Long.parseLong(numString);
    }

    public static String toFormattedMoney(Context context, Long number){
        MoneyFormatter formatter = PrefUtil.getMoneyFormatter(context);
        String str = formatMoney(formatter, number);
        return formatMoney(formatter, number);
    }

    public static String formatMoney(MoneyFormatter formatter, long money){
        AppMoney appMoney = new AppMoney(money);
        if(formatter.isShortType) appMoney.setShortType();
        if(formatter.hasCurrencySymbol) appMoney.currencySymbol = "đ";
        appMoney.setHasFraction(formatter.hasFraction);
        appMoney.setSeparator(formatter.separator);
        return appMoney.toString();
    }

    public static String toPercent(float value, float total){
        return String.format("%.1f", value*100.0/total)+"%";
    }
}
