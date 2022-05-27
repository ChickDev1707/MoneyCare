package com.example.moneycare.utils;

import androidx.databinding.InverseMethod;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

public class Converter {

    private static DecimalFormat formatter;
    static {
        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
    }
    @InverseMethod("stringToLong")
    public static String longToString(Long number) {
        if(number == 0L) return "";
        return number.toString();
    }

    public static Long stringToLong(String numString) {
        if(numString.equals("")) return 0L;
        else return Long.parseLong(numString);
    }

    public static String toCurrency(Long number){
        return formatter.format(number);
    }
}
