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
    private Long budget;
    private Long money_spent;
    private Date date;

    public Budget(){}

    public Budget(String group_id, Long budget, Long money_spent, Date date) {
        this.group_id = group_id;
        this.budget = budget;
        this.money_spent = money_spent;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getMoney_spent() {
        return money_spent;
    }

    public void setMoney_spent(Long money_spent) {
        this.money_spent = money_spent;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
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
        HashMap<String, Object> result = new HashMap<>();
        result.put("group_id", group_id);
        result.put("date", date.getTime());
        result.put("budget", budget);
        result.put("money_spent", money_spent);
        return result;
    }

    public static Budget fromMap(Map<String, Object> map) {
        return new Budget(
                ((DocumentReference) map.get("group_id")).getPath(),
                ((Long) map.get("budget")),
                ((Long) map.get("money_spent")),
                ((Timestamp) map.get("date")).toDate()
        );
    }
}
