package com.example.moneycare.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class User {

    public String name;
    public String email;
    public String photoUrl;

    public User(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("photoUrl", photoUrl);
        return result;
    }
    public static User fromMap(Map<String, Object> map){
        return new User(
            (String) map.get("name"),
            (String) map.get("email"),
            (String) map.get("photoUrl")
        );
    }
}