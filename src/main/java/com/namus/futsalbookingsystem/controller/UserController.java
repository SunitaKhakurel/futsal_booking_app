package com.namus.futsalbookingsystem.controller;

import com.namus.futsalbookingsystem.entity.AppUser;
import com.namus.futsalbookingsystem.entity.AuthResult;
import com.namus.futsalbookingsystem.entity.EditAdminProfile;
import com.namus.futsalbookingsystem.entity.PasswordChangeRequest;
import com.namus.futsalbookingsystem.repository.UserRepository;
import com.namus.futsalbookingsystem.response.ApiResponse;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Validated
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/addNewUser")
    public ResponseEntity<ApiResponse> addNewUser(@Valid @RequestBody AppUser appUser) {
        try {
            List<AppUser> list = userRepository.findByPhone(appUser.getPhone());
            if (!list.isEmpty()) {
                ApiResponse apiResponse = new ApiResponse("user already exist", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            }
            service.saveUser(appUser);
            ApiResponse apiResponse = new ApiResponse("Success", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body((apiResponse));
        } catch (ValidationException v) {

            ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AppUser appUser) {
        try {
            AuthResult authResult = service.findUserByPhoneAndPassword(appUser);
            service.updateDeviceToken(appUser,appUser.getPhone());
            List<AppUser> appUserList = authResult.getAppUsers();

            if (appUserList != null && !appUserList.isEmpty()) {
                String token = jwtService.generateToken(appUserList.get(0).getUserName());

                ApiResponse apiResponse = new ApiResponse("Login successful", HttpStatus.OK.value(), token, appUserList.get(0).getRole());
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



    @PostMapping("/forgotPassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody AppUser appUser) {
        try {
            String message = service.changePassword(appUser);
            ApiResponse apiResponse = new ApiResponse(message, HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (ValidationException v) {
            ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }


    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@Valid @RequestBody AppUser authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUserName());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    //Admin
    @PreAuthorize("hasAuthority('SuperAdmin')")
    @PostMapping("/addNewAdmin")
    public ResponseEntity<ApiResponse> addNewAdmin(@Valid @RequestBody AppUser appUser) {
        System.out.println("user" + appUser.getUserName());

        System.out.println(appUser.getUserName());
        if (appUser.getUserName() != null) {
            System.out.println("user" + appUser.getUserName());
            try {
                service.updateAdmin(appUser);
                ApiResponse apiResponse = new ApiResponse("Success", HttpStatus.OK.value());
                return ResponseEntity.status(HttpStatus.OK).body((apiResponse));
            } catch (ValidationException v) {
                ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            }
            catch (Exception e) {
                ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
            }
        } else {
            try {
                List<AppUser> list = userRepository.findByPhone(appUser.getPhone());
                if (!list.isEmpty()) {
                    ApiResponse apiResponse = new ApiResponse("user already exist", HttpStatus.BAD_REQUEST.value());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
                }

                service.saveAdmin(appUser);

                ApiResponse apiResponse = new ApiResponse("Success", HttpStatus.OK.value());
                return ResponseEntity.status(HttpStatus.OK).body((apiResponse));
            } catch (ValidationException v) {

                ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            } catch (Exception e) {
                ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
            }
        }
    }


    @PostMapping("/getAdminDetails")
    public ResponseEntity<ApiResponse> listAllAdmin() {
        try {
            List<AppUser> adminList = service.getAdminDetails();
            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), adminList);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }

    @PreAuthorize("hasAuthority('Admin') OR hasAuthority('User')")
    @PostMapping("/editAdminProfile/{phone}")
    public ResponseEntity<ApiResponse> updateAppUser(@Valid @RequestBody EditAdminProfile editAdminProfile, @PathVariable("phone") long phone) {

        try {
            service.updateAdminProfile(editAdminProfile,phone);
            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (ValidationException v) {
            ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PreAuthorize("hasAuthority('SuperAdmin')")
    @PostMapping("/deleteAdmin")
    public ResponseEntity<ApiResponse> deleteAdmin(@Valid @RequestBody AppUser appUser) {
        System.out.println("hello");
        try {
            service.deleteAdmin(appUser);
            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (NoSuchElementException e) {
            ApiResponse apiResponse = new ApiResponse("Student not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }

    @PreAuthorize("hasAuthority('Admin') OR hasAuthority('SuperAdmin') OR hasAuthority('User') ")
    @GetMapping("/adminDetails/{phone}")
    public ResponseEntity<ApiResponse> userDetails(@Valid @PathVariable("phone") long phone) {

        try {

            List<AppUser> appUsers = service.getUserByPhoneNumber(phone);

            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), appUsers.get(0));
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/changePassword/{phone}")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest, @PathVariable("phone") long phone){
        try {
            String message =service.changePassword(passwordChangeRequest,phone);
            ApiResponse apiResponse = new ApiResponse(message, HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (ValidationException v) {
            ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }


    //

    @PostMapping("/addNewSuperAdmin")
    public ResponseEntity<ApiResponse> addNewSuperAdmin(@Valid @RequestBody AppUser appUser) {
        try {
            List<AppUser> list = userRepository.findByPhone(appUser.getPhone());
            if (!list.isEmpty()) {
                ApiResponse apiResponse = new ApiResponse("user already exist", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            }
            service.saveSuperAdmin(appUser);
            ApiResponse apiResponse = new ApiResponse("Success", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body((apiResponse));
        } catch (ValidationException v) {

            ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

}
