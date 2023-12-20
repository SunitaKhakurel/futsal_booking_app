package com.namus.futsalbookingsystem.controller;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.namus.futsalbookingsystem.entity.*;
import com.namus.futsalbookingsystem.repository.FutsalRepository;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
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
    private FutsalRepository futsalRepository;

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

//    @PostMapping("/changePassword/{phone}")
//    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest, @PathVariable("phone") long phone){
//        try {
//            String message =
//            ApiResponse apiResponse = new ApiResponse(message, HttpStatus.OK.value());
//            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//        } catch (ValidationException v) {
//            ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
//        } catch (Exception e) {
//            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
//        }
//    }
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


    @PostMapping("getAdminDetails")
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

    @PostMapping("/editAdminProfile/{phone}")
    public ResponseEntity<ApiResponse> updateAdmin(@Valid @RequestBody EditAdminProfile editAdminProfile, @PathVariable("phone") long phone) {

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

    @PostMapping("/saveFutsal")
    public ResponseEntity<ApiResponse> saveFutsal(@Valid @RequestBody Futsal futsal) {
        try {
            Optional<Futsal> futsal1 = futsalRepository.findByPhone(futsal.getPhone());
            if (!futsal1.isEmpty()) {
                ApiResponse apiResponse = new ApiResponse("Futsal with same phone number already exist", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            }
            futsalService.saveFutsal(futsal);
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

    @GetMapping("/getFutsalDetails")
    public ResponseEntity<ApiResponse> listAllFutsal() {
        try {
            List<Futsal> adminList = futsalService.getAllFutsalData();
            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), adminList);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }

    @GetMapping("/getFutsalData")
    public List<Futsal> getFutsalData() {
        return futsalService.getAllFutsalData(); // Implement a service method to fetch the data
    }




    @GetMapping("/adminDetails/{phone}")
    public ResponseEntity<ApiResponse> adminDetails(@Valid @PathVariable("phone") long phone) {

        try {

            List<AppUser> appUsers = service.getUserByPhoneNumber(phone);

            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), appUsers.get(0));
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }


    @GetMapping("/futsalDetails/{phone}")
    public ResponseEntity<ApiResponse> futsalDetails(@Valid @PathVariable("phone") long phone) {

        try {

            Futsal futsal = futsalService.getFutsalByPhoneNumber(phone);

            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), futsal);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }


    @PutMapping("/updateFutsal/{phone}")
    public ResponseEntity<ApiResponse> updateFutsal(@Valid @RequestBody Futsal futsal, @PathVariable("phone") long phone) {

        try {
            futsalService.updateFutsalDetails(futsal, phone);
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

    @DeleteMapping("/deleteFutsal/{phone}")
    public ResponseEntity<ApiResponse> deleteFutsal(@Valid @PathVariable("phone") long phone) {
        try {
            System.out.println(phone);
            futsalService.deleteFutsal(phone);
            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (NoSuchElementException e) {
            ApiResponse apiResponse = new ApiResponse("futsal not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }


    @PostMapping("/addNewEvent")
    public ResponseEntity<ApiResponse> addNewEvent(@Valid @RequestBody Events event) {
        try {
            futsalService.addEvent(event);
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


    @GetMapping("/eventDetails")
    public ResponseEntity<ApiResponse> eventDetails() {

        try {
           List<Events> eventsList= futsalService.eventsDetails();

            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), eventsList);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PutMapping("/updateEvent/{id}")
    public ResponseEntity<ApiResponse> updateEvent(@Valid @RequestBody Events event, @PathVariable("id") int id) {

        try {
            futsalService.updateEventDetails(event, id);
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

    @DeleteMapping("/deleteEvent/{id}")
    public ResponseEntity<ApiResponse> deleteEvent(@Valid @PathVariable("id") int id) {
        try {
            futsalService.deleteEvent(id);
            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (NoSuchElementException e) {
            ApiResponse apiResponse = new ApiResponse("EVent not found", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @PostMapping("/bookFutsal")
    public ResponseEntity<ApiResponse> bookFutsal(@Valid @RequestBody BookingInfo bookingInfo) {
        try {
            futsalService.bookFutsal(bookingInfo);
            Futsal futsal=futsalService.getFutsalByFutsalName(bookingInfo.getFutsalName());
            List<String> futsalDeviceToken=futsal.getFutsalDeviceToken();

            try {
                for(String deviceToken:futsalDeviceToken) {
                    Message message = Message.builder()
                            .setToken(deviceToken)
                            .setNotification(Notification.builder()
                                    .setTitle(bookingInfo.getTitle())
                                    .setBody(bookingInfo.getMessageBody())
                                    .build()).putData("futsalName", bookingInfo.getFutsalName())
                            .build();

                    String response = FirebaseMessaging.getInstance().send(message);
                }
            }
            catch (FirebaseMessagingException e) {
                String errorMessage = "Error sending notification: " + e.getMessage();
                ApiResponse apiResponse = new ApiResponse("Bad Request", HttpStatus.BAD_REQUEST.value(),errorMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            }

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

    @PostMapping("/registerTeam")
    public ResponseEntity<ApiResponse> registerTeam(@Valid @RequestBody RegisterTeam registerTeam) {
        try {
                futsalService.registerTeam(registerTeam);
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

    @GetMapping("/eventDetailsAccordingToFutsalName/{futsalName}")
    public ResponseEntity<ApiResponse> eventDetailsAccordingToFutsalName(@PathVariable("futsalName") String futsalName) {

        try {
            List<Events> eventsList= futsalService.getEventAccordingToFutsalName(futsalName);

            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), eventsList);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @GetMapping("/registerationDetailAccordingToFutsalName/{futsalName}")
    public ResponseEntity<ApiResponse> regDetailsByFutsal(@PathVariable("futsalName") String futsalName) {

        try {
           List<RegisterTeam> teams=futsalService.getregInfoByFutsalName(futsalName);

            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), teams);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('User')")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hi")
    @PreAuthorize("hasAuthority('Admin')")
    public String hi() {
        return "hi";
    }

}

