package com.namus.futsalbookingsystem.entity;

import java.util.List;

public class AuthResult {
    private List<User> users;
    private String message;

    public AuthResult(List<User> users, String message) {
        this.users = users;
        this.message = message;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getMessage() {
        return message;
    }
}
