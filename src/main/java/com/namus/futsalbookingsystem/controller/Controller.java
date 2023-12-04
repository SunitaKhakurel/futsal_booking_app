package com.namus.futsalbookingsystem.controller;

import com.namus.futsalbookingsystem.entity.*;
import com.namus.futsalbookingsystem.repository.UserRepository;
import com.namus.futsalbookingsystem.response.ApiResponse;
import com.namus.futsalbookingsystem.service.FutsalService;
import com.namus.futsalbookingsystem.service.JwtService;
import com.namus.futsalbookingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController

public class Controller {

    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    FutsalService futsalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/addNewUser")
    public ResponseEntity<ApiResponse> addNewUser(@RequestBody User user) {
        try {
            List<User> list= userRepository.findByPhone(user.getPhone());
                 if(!list.isEmpty()){
                     ApiResponse apiResponse = new ApiResponse("user already exist", HttpStatus.BAD_REQUEST.value());
                     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
                 }

            service.saveUser(user);
            ApiResponse apiResponse = new ApiResponse("Success", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body((apiResponse));
        }catch(ValidationException v) {

            ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PostMapping("/loginUser")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            AuthResult authResult = service.findUserByPhoneAndPassword(user);
            List<User> userList = authResult.getUsers();

            if (userList != null && !userList.isEmpty()) {
                String token = jwtService.generateToken(userList.get(0).getUserName());
                ApiResponse apiResponse = new ApiResponse("Login successful", HttpStatus.OK.value(), List.of(token));
                return ResponseEntity.ok(apiResponse);
            } else {
                ApiResponse apiResponse = new ApiResponse(authResult.getMessage(), HttpStatus.UNAUTHORIZED.value());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Add this line to print the exception stack trace
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }


    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody User user){
        try{
           String message= service.changePassword(user);
            ApiResponse apiResponse = new ApiResponse(message, HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }catch (ValidationException v) {
            ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody User authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUserName());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    //Admin

    @PostMapping("/addNewAdmin")
    public ResponseEntity<ApiResponse> addNewAdmin(@RequestBody Admin admin) {
        try {
            List<User> list= userRepository.findByPhone(admin.getPhone());
            if(!list.isEmpty()){
                ApiResponse apiResponse = new ApiResponse("user already exist", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            }

            service.saveAdmin(admin);
            ApiResponse apiResponse = new ApiResponse("Success", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body((apiResponse));
        }catch(ValidationException v) {

            ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PostMapping("/loginAdmin")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {
        try {
            AuthResultForAdmin authResult = service.findAdminByPhoneAndPassword(admin);
            List<Admin> adminList = authResult.getUsers();

            if (adminList != null && !adminList.isEmpty()) {
                String token = jwtService.generateToken(adminList.get(0).getUserName());
                ApiResponse apiResponse = new ApiResponse("Login successful", HttpStatus.OK.value(), List.of(token));
                return ResponseEntity.ok(apiResponse);
            } else {
                ApiResponse apiResponse = new ApiResponse(authResult.getMessage(), HttpStatus.UNAUTHORIZED.value());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Add this line to print the exception stack trace
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('User')")
    public String hello(){
        return"hello";
    }

    @GetMapping("/hi")
    @PreAuthorize("hasAuthority('Admin')")
    public String hi(){
        return"hi";
    }

}

