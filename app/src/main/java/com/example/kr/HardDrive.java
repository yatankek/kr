package com.example.kr;

import java.util.HashMap;
import java.util.Map;

public class HardDrive {
    private String model;
    private int capacity;
    private String descen;
    private String descru;
    private double price;

    public HardDrive() {}

    public HardDrive(String model, int capacity, String descen, String descru, double price) {
        this.model = model;
        this.capacity = capacity;
        this.descen = descen;
        this.descru = descru;
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescen() {
        return descen;
    }

    public void setDescen(String descen) {
        this.descen = descen;
    }

    public String getDescru() {
        return descru;
    }

    public void setDescru(String descru) {
        this.descru = descru;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("model", model);
        result.put("capacity", capacity);
        result.put("descen", descen);
        result.put("descru", descru);
        result.put("price", price);
        return result;
    }
}
