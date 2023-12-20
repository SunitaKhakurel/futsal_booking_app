package com.namus.futsalbookingsystem.service;

import com.namus.futsalbookingsystem.App;
import com.namus.futsalbookingsystem.entity.AppUser;
import com.namus.futsalbookingsystem.entity.AuthResult;
import com.namus.futsalbookingsystem.entity.EditAdminProfile;
import com.namus.futsalbookingsystem.entity.Futsal;

import java.util.List;

public interface UserService{
    public String saveUser(AppUser appUser);
    public AuthResult findUserByPhoneAndPassword(AppUser appUser);
    public String changePassword(AppUser appUser);
    public List<AppUser> getUserByPhoneNumber(long phone);

    public String saveAdmin(AppUser appUser);
    public List<AppUser> getAdminDetails();
    public void updateAdmin(AppUser appUser);
    public void deleteAdmin(AppUser appUser);

    public void updateAdminProfile(EditAdminProfile editAdminProfile, long phone);


}

