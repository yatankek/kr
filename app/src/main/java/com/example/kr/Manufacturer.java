package com.example.kr;

import java.util.HashMap;
import java.util.Map;

public class Manufacturer {
    private String name;

    public Manufacturer() {}

    public Manufacturer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        return result;
    }
}
