package com.example.moneycare.data.custom;

import com.example.moneycare.utils.appenum.MoneySeparator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class AppMoney {
    private long money;

    public double value;
    public String shortType;
    public String currencySymbol;

    DecimalFormat df;
    String pattern = "";

    // use different locale to set different separator
    // VI is dot separator dot and en for comma
    Locale locale = null;
    public AppMoney(long money){
        this.money = money;
        this.value = (double) money;
        shortType = "";
        currencySymbol = "";
    }

    public void setHasFraction(boolean hasFraction){
        if(hasFraction){
            pattern = "#,##0.00";
        }else{
            int noDigits = countDigitsAfterPoint(value);
            if(noDigits< 2) pattern = "";
            else pattern = "#,##0.00";
        }
    }
    private int countDigitsAfterPoint(double value){
        String[] splitter = Double.toString(value).split("\\.");
        return splitter[1].length();   // After  Decimal Count
    }
    public void setSeparator(MoneySeparator moneySeparator){
        if(moneySeparator.equals(MoneySeparator.DOT)) locale = new Locale("vi");
        else locale = new Locale("en_US");
    }
    public void setShortType(){
        String[] shortTypes = {"", "K", "M", "B", "T"};
        int index = getIndexShortTypeIndex();
        if(index != -1){
            value = (double) (money/(Math.pow(1000, index)));
            shortType = shortTypes[index];
        }
    }
    private int getIndexShortTypeIndex(){
        int index = -1;
        if(money == 0) index = 0;
        else{
            long clone = money;
            while(clone != 0){
                clone = clone/1000;
                index++;
            }
        }
        return index;
    }
    public String toString(){
        df = new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
        String valueString = df.format(value);
        return String.format("%s%s %s", valueString, shortType, currencySymbol);
    }
}