package com.example.moneycare.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransactionGroup {
    private String id;
    private String image;
    private String name;
    private Boolean type;

    public TransactionGroup() {}
    public TransactionGroup(String image, String name, Boolean type) {
        this.image = image;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
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
        result.put("image", image);
        result.put("name", name);
        result.put("type", type);
        return result;
    }

    public static TransactionGroup fromMap(Map<String, Object> map) {
        return new TransactionGroup(
                (String) map.get("image"),
                ((String) map.get("name")),
                ((Boolean) map.get("type"))
        );
    }

    @Override
    public String toString() {
        return name;
    }
}
