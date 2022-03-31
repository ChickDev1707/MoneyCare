package com.example.moneycare.data.model;

import java.util.Date;

public class Transaction {
    public double money;
    public Date date;
    public Transaction(double money, Date date){
        this.money = money;
        this.date = date;
    }
}
