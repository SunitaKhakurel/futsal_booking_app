package com.namus.futsalbookingsystem.service;

import com.namus.futsalbookingsystem.entity.AuthResult;
import com.namus.futsalbookingsystem.entity.AppUser;

import java.util.List;

public interface UserService{
    public String saveUser(AppUser appUser);
    public AuthResult findUserByPhoneAndPassword(AppUser appUser);
    public String changePassword(AppUser appUser);
    public String saveAdmin(AppUser appUser);
    public List<AppUser> getAdminDetails();

}

