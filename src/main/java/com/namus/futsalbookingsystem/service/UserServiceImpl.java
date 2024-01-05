package com.namus.futsalbookingsystem.service;


import com.namus.futsalbookingsystem.entity.*;
import com.namus.futsalbookingsystem.repository.FutsalRepository;
import com.namus.futsalbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements  UserService, UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FutsalRepository futsalRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public String saveUser(AppUser appUser) {
        try{
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            appUser.setRole("User");
            appUser.setUserName(appUser.getName() + appUser.getPhone());
            userRepository.save(appUser);
            return "saved";
        }catch(Exception e){
            return e.toString();
        }

    }



    @Override
    public AuthResult findUserByPhoneAndPassword(AppUser appUser) {

       List<AppUser> appUsers =userRepository.findByPhone(appUser.getPhone());
       if(!appUsers.isEmpty()) {
           if(appUsers !=null && passwordEncoder.matches(appUser.getPassword(), appUsers.get(0).getPassword())){
               return new AuthResult(appUsers, "Authentication successful");
           } else {
               return new AuthResult(null, "Invalid credentials");
           }
       } else {
           return new AuthResult(null, "User not found");
       }
       }

    @Override
    public String changePassword(AppUser appUser) {
        List<AppUser> appUsers =userRepository.findByPhone(appUser.getPhone());
        AppUser appUserDetails = appUsers.get(0);
        if(appUserDetails !=null){
            appUserDetails.setPassword(passwordEncoder.encode(appUser.getPassword()));
            userRepository.save(appUserDetails);
            return "Password updated";
        }else{
            return "User Not found";
        }
    }


    @Override
    public String saveAdmin(AppUser appUser) {
        try{
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            appUser.setRole("Admin");
            appUser.setUserName(appUser.getName() + appUser.getPhone());
            userRepository.save(appUser);
            return "saved";
        }catch(Exception e){
            return e.toString();
        }
    }

    @Override
    public List<AppUser> getAdminDetails() {
        List<AppUser> adminList=userRepository.findByRole("Admin");
        return adminList;
    }

    @Override
    public List<AppUser> getUserByPhoneNumber(long phone) {
        List<AppUser> appUsers=userRepository.findByPhone(phone);
        return appUsers;
    }

    @Override
    public String changePassword(PasswordChangeRequest passwordChangeRequest, long phone) {
        List<AppUser> appUsers=getUserByPhoneNumber(phone);
        AppUser appUser=appUsers.get(0);
        String password=appUser.getPassword();
        System.out.println("p"+password);
        String currentPassword=passwordEncoder.encode(passwordChangeRequest.getCurrentPassword());
        System.out.println("c"+currentPassword);
        String newPassword=passwordEncoder.encode(passwordChangeRequest.getNewPassword());
        System.out.println("n"+newPassword);
        if(passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(),password)){
            appUser.setPassword(newPassword);
            userRepository.save(appUser);
            return "Password Updated";
        }else{
            return "password didnot match";
        }

    }

    @Override
    public void updateAdmin(AppUser appUser) {
        AppUser appUser1= userRepository.findByUserName(appUser.getUserName());
                if(appUser1!=null){
                    appUser1.setName(appUser.getName());
                    appUser1.setAddress(appUser.getAddress());
                    appUser1.setEmail(appUser.getEmail());
                    //appUser1.setPassword(passwordEncoder.encode(appUser.getPassword()));
                    appUser1.setGender(appUser.getGender());
                    appUser1.setUserName(appUser.getName()+ appUser.getPhone());
                    userRepository.save(appUser1);
                }

    }

    @Override
    public void deleteAdmin(AppUser appUser) {
        AppUser appUser1= userRepository.findByUserName(appUser.getUserName());
        if(appUser1!=null){
            userRepository.deleteById(appUser1.getId());
        }
    }

    @Override
    public void updateDeviceToken(AppUser appUser,long phone) {
        List<AppUser> appUser1=getUserByPhoneNumber(phone);
        AppUser appUser2=appUser1.get(0);
        if(appUser2!=null) {
        appUser2.setFutsalDeviceToken(appUser.getFutsalDeviceToken());
        userRepository.save(appUser2);
        }
    }

    @Override
    public void updateAdminProfile(EditAdminProfile appUser, long phone) {
        List<AppUser> appUser1=getUserByPhoneNumber(phone);
        AppUser appUser2=appUser1.get(0);
        if(appUser2!=null){
            appUser2.setName(appUser.getAdminName());
            appUser2.setEmail(appUser.getEmail());
            appUser2.setAddress(appUser.getAddress());
           appUser2.setImage(appUser.getImage());
            userRepository.save(appUser2);
        }

    }

    @Override
    public String saveSuperAdmin(AppUser appUser) {
        try{
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            appUser.setRole("SuperAdmin");
            appUser.setUserName(appUser.getName() + appUser.getPhone());
            userRepository.save(appUser);
            return "saved";
        }catch(Exception e){
            return e.toString();
        }
    }

    @Override
    public AppUser getUserDetailsAccToEmail(String email) {
        Optional<AppUser> appUser=userRepository.findByEmail(email);
        return appUser.orElse(null);
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<AppUser> userDetails=userRepository.findByuserName(userName);
        return userDetails.map(UserInfoDetails::new).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }


}

