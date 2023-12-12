package com.namus.futsalbookingsystem.entity;

import java.util.List;

public class AuthResult {
    private List<AppUser> appUsers;
    private String message;

    public AuthResult(List<AppUser> appUsers, String message) {
        this.appUsers = appUsers;
        this.message = message;
    }

    public List<AppUser> getAppUsers() {
        return appUsers;
    }

    public String getMessage() {
        return message;
    }
}
