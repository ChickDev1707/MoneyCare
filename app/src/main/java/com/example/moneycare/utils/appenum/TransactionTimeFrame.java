package com.example.moneycare.utils.appenum;

public enum TransactionTimeFrame {
    DAY(1),
    WEEK(2),
    MONTH(3),
    YEAR(4);
    public final int value;
    private TransactionTimeFrame(int val){
        this.value = val;
    }
    public int getValue(){
        return value;
    }
    public static TransactionTimeFrame getTimeFrame(int val){
        switch (val){
            case 1:
                return TransactionTimeFrame.DAY;
            case 2:
                return TransactionTimeFrame.WEEK;
            case 3:
                return TransactionTimeFrame.MONTH;
            case 4:
                return TransactionTimeFrame.YEAR;
            default:
                return null;
        }
    }
}

