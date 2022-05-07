package com.example.moneycare.data.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public String id;
    public String name;
    public long money;
    public String image;
    public Wallet(){}
    public Wallet(String id, String name, long money, String image){
        this.id = id;
        this.name = name;
        this.money = money;
        this.image = image;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("money", money);
        result.put("image", image);
        return result;
    }
    public static Wallet fromMap(String id, Map<String, Object> map){
        return new Wallet(
                id,
                map.get("name").toString(),
                (Long) map.get("money"),
                map.get("image").toString()
        );
    }
}
