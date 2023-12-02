package com.namus.futsalbookingsystem.service;


import com.namus.futsalbookingsystem.entity.AuthResult;
import com.namus.futsalbookingsystem.entity.User;
import com.namus.futsalbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements  UserService, UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public String saveUser(User user) {
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("User");
            user.setUserName(user.getName());
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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userDetails=userRepository.findByuserName(userName);
        return userDetails.map(UserInfoDetails::new).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }
}

