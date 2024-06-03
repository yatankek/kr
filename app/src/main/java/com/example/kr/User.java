package com.example.kr;

public class User {
    private String email;
    private String role;

    // Конструктор по умолчанию, необходимый для вызова DataSnapshot.getValue(User.class)
    public User() {}

    public User(String email, String role) {
        this.email = email;
        this.role = role;
    }

    // Геттеры и сеттеры
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
