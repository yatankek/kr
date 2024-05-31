package com.example.kr;

public class HardDrive {
    private String model;
    private int capacity;
    private double price;

    public HardDrive(String model, int capacity, double price) {
        this.model = model;
        this.capacity = capacity;
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPrice() {
        return price;
    }
}
