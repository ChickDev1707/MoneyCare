package com.example.moneycare.utils;

import com.example.moneycare.data.model.Budget;

import java.text.DecimalFormat;

public class Convert {
    public static Long convertToNumber(String str){
        try {
            String[] arrs = str.split(",");
            return Long.valueOf(String.join("", arrs));
        }
        catch(Exception e){
            return -1L;
        }
    }
    public static String convertToThousandsSeparator(Long number){
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            return decimalFormat.format(number);
        }
        catch(Exception e){
            return "";
        }
    }
    public static String convertToMoneyCompact(Long number){
        try {
            Double tmp = Math.round(number.doubleValue() * 100.00 / 1000) / 100.00;
            String str = tmp + " K";
            Long count = 0L;
            while(tmp >= 1000 ){
                tmp = Math.round(tmp * 100.00 / 1000) / 100.00;
                count ++;
                if(count == 1){
                    str = tmp + " M";
                }
                else {
                    str = tmp + " B";
                }
            }
            return str;
        }
        catch(Exception e){
            return "";
        }
    }
}
