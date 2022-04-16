package com.example.moneycare.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserTransaction {
    public long money;
    public Date date;
    public String group;
    public String note;
    public String wallet;
    public UserTransaction(long money, String group, String note, Date date, String wallet){
        this.money = money;
        this.date = date;
        this.group = group;
        this.note = note;
        this.wallet = wallet;
    }
    @Exclude
    public Map<String, Object> toMap() {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        HashMap<String, Object> result = new HashMap<>();
        result.put("money", money);
        result.put("date", new Timestamp(date));
        result.put("group", fs.document(group));
        result.put("note", note);
        result.put("wallet", fs.document(wallet));
        return result;
    }
    public static UserTransaction fromMap(Map<String, Object> map){
        Timestamp ts = (Timestamp) map.get("date");
        return new UserTransaction(
            (Long) map.get("money"),
            ((DocumentReference) map.get("group")).getPath(),
            ((String) map.get("note")),
            ts.toDate(),
            ((DocumentReference) map.get("wallet")).getPath()
        );
    }
}
