package com.example.moneycare.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.InverseMethod;
import androidx.preference.PreferenceManager;

import com.example.moneycare.data.custom.AppMoney;
import com.example.moneycare.data.custom.MoneyFormatter;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

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

}
