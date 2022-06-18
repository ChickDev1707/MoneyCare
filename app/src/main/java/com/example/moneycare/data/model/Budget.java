package com.example.moneycare.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Budget {
    private String id;
    private String group_id;
    private Long budgetOfMonth;
    private Date date;

    public Budget(){}

    public Budget(String group_id, Long budget, Date date) {
        this.group_id = group_id;
        this.budgetOfMonth = budget;
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getBudgetOfMonth() {
        return budgetOfMonth;
    }

    public void setBudget(Long money) {
        this.budgetOfMonth = money;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        HashMap<String, Object> result = new HashMap<>();
        result.put("group_id",fs.document(group_id));
        result.put("date", date);
        result.put("budgetOfMonth", budgetOfMonth);
        return result;
    }

    public static Budget fromMap(Map<String, Object> map) {
        return new Budget(
                ((DocumentReference) map.get("group_id")).getPath(),
                ((Long) map.get("budgetOfMonth")),
                ((Timestamp) map.get("date")).toDate()
        );
    }
}
