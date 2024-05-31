package com.example.kr;

public class Manufacturer {
    private String name;

    public Manufacturer() {
        // Пустой конструктор, необходимый для вызовов к Firestore
    }

    public Manufacturer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
