package com.example.moneycare.data.model;

import java.util.Map;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class User {

    public String name;
    public int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static User fromMap(Map<String, Object> map){
        return new User(
            (String) map.get("name"),
            (Integer) map.get("age")
        );
    }
}