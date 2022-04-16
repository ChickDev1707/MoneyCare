package com.example.moneycare.data.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public String name;
    public long money;
    public Wallet(){}
    public Wallet(String name, long money){
        this.name = name;
        this.money = money;
    }
    @Exclude
    public Map<String, Object> toMap() {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("money", money);

        return result;
    }
    public static Wallet fromMap(Map<String, Object> map){
        return new Wallet(
                map.get("name").toString(),
                (Long) map.get("money")
        );
    }
}
