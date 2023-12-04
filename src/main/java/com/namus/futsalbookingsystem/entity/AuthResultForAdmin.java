package com.namus.futsalbookingsystem.entity;

import java.util.List;

public class AuthResultForAdmin {

    private List<Admin> admins;
    private String message;

    public AuthResultForAdmin(List<Admin> admins, String message) {
        this.admins = admins;
        this.message = message;
    }

    public List<Admin> getUsers() {
        return admins;
    }

    public String getMessage() {
        return message;
    }
}
