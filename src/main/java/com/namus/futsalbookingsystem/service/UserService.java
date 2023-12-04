package com.namus.futsalbookingsystem.service;

import com.namus.futsalbookingsystem.entity.Admin;
import com.namus.futsalbookingsystem.entity.AuthResult;
import com.namus.futsalbookingsystem.entity.AuthResultForAdmin;
import com.namus.futsalbookingsystem.entity.User;

public interface UserService{
    public String saveUser(User user);
    public AuthResult findUserByPhoneAndPassword(User user);
    public String changePassword(User user);

    public String saveAdmin(Admin admin);

    public AuthResultForAdmin findAdminByPhoneAndPassword(Admin admin);

}

