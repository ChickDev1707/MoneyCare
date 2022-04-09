package com.example.moneycare.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
    public long money;
    public Date date;
    public String group;
    public String wallet;
    public Transaction(long money, Date date, String group, String wallet){
        this.money = money;
        this.date = date;
        this.group = group;
        this.wallet = wallet;
    }
    @Exclude
    public Map<String, Object> toMap() {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        HashMap<String, Object> result = new HashMap<>();
        result.put("money", money);
        result.put("date", date.getTime());
        result.put("group", fs.document(group));
        result.put("wallet", fs.document(wallet));

        return result;
    }
    public static Transaction fromMap(Map<String, Object> map){
        return new Transaction(
            (Long) map.get("money"),
            ((Timestamp) map.get("date")).toDate(),
            ((DocumentReference) map.get("group")).getPath(),
            ((DocumentReference) map.get("wallet")).getPath()
        );
    }
}
