package com.namus.futsalbookingsystem.service;


import com.namus.futsalbookingsystem.entity.Admin;
import com.namus.futsalbookingsystem.entity.AuthResult;
import com.namus.futsalbookingsystem.entity.AuthResultForAdmin;
import com.namus.futsalbookingsystem.entity.User;
import com.namus.futsalbookingsystem.repository.AdminRepository;
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
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public String saveUser(User user) {
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("Admin");
            user.setUserName(user.getName() + user.getPhone());
            userRepository.save(user);
            return "saved";
        }catch(Exception e){
            return e.toString();
        }

    }



    @Override
    public AuthResult findUserByPhoneAndPassword(User user) {

       List<User> users=userRepository.findByPhone(user.getPhone());
       if(!users.isEmpty()) {
           if(users!=null && passwordEncoder.matches(user.getPassword(),users.get(0).getPassword())){
               return new AuthResult(users, "Authentication successful");
           } else {
               return new AuthResult(null, "Invalid credentials");
           }
       } else {
           return new AuthResult(null, "User not found");
       }
       }

    @Override
    public String changePassword(User user) {
        List<User> users=userRepository.findByPhone(user.getPhone());
        User userDetails=users.get(0);
        if(userDetails!=null){
            userDetails.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(userDetails);
            return "Password updated";
        }else{
            return "User Not found";
        }
    }

    @Override
    public String saveAdmin(Admin admin) {
        try{
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setRole("User");
            admin.setUserName(admin.getName() + admin.getPhone());
            adminRepository.save(admin);
            return "saved";
        }catch(Exception e){
            return e.toString();
        }
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userDetails=userRepository.findByuserName(userName);
        return userDetails.map(UserInfoDetails::new).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }




    @Override
    public AuthResultForAdmin findAdminByPhoneAndPassword(Admin admin) {

        List<Admin> admins=adminRepository.findByPhone(admin.getPhone());
        if(!admins.isEmpty()) {
            if(admins!=null && passwordEncoder.matches(admin.getPassword(),admins.get(0).getPassword())){
                return new AuthResultForAdmin(admins, "Authentication successful");
            } else {
                return new AuthResultForAdmin(null, "Invalid credentials");
            }
        } else {
            return new AuthResultForAdmin(null, "User not found");
        }
    }
}

