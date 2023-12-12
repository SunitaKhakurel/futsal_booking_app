package com.namus.futsalbookingsystem.service;


import com.namus.futsalbookingsystem.entity.AppUser;
import com.namus.futsalbookingsystem.entity.AuthResult;
import com.namus.futsalbookingsystem.entity.Futsal;
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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<AppUser> userDetails=userRepository.findByuserName(userName);
        return userDetails.map(UserInfoDetails::new).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }


}

