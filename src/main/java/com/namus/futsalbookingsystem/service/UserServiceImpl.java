package com.namus.futsalbookingsystem.service;


import com.namus.futsalbookingsystem.entity.AuthResult;
import com.namus.futsalbookingsystem.entity.AppUser;
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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<AppUser> userDetails=userRepository.findByuserName(userName);
        return userDetails.map(UserInfoDetails::new).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }


}

