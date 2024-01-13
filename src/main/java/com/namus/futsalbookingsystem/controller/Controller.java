package com.namus.futsalbookingsystem.controller;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.namus.futsalbookingsystem.App;
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
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Validated
public class Controller {

    @Autowired
    FutsalService futsalService;


    @Autowired
    private FutsalRepository futsalRepository;
    @Autowired
    private UserService userService;


    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/saveFutsal")
    public ResponseEntity<ApiResponse> saveFutsal(@Valid @RequestBody Futsal futsal) {
        try {
            Optional<Futsal> futsal1 = futsalRepository.findByPhone(futsal.getPhone());
            if (!futsal1.isEmpty()) {
                ApiResponse apiResponse = new ApiResponse("Futsal with same phone number already exist", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            }
            Futsal futsal2=futsalService.getFutsalByFutsalName(futsal.getFutsalName());
            if (futsal2!=null) {
                ApiResponse apiResponse = new ApiResponse("Futsal with same name number already exist", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
            }
            futsal.setAvailableTimeList(futsal.getOriginalTimeList());
            futsalService.saveFutsal(futsal);
            LocalTime currentTime = LocalTime.now();
            System.out.println("Current time: " + currentTime);
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
            List<Futsal> futsalList = futsalService.getAllFutsalData();
            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), futsalList);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }

//    @GetMapping("/getFutsalData")
//    public List<Futsal> getFutsalData() {
//        return futsalService.getAllFutsalData(); // Implement a service method to fetch the data
//    }






    @PreAuthorize("hasAuthority('Admin')")
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

    @PreAuthorize("hasAuthority('Admin')")
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
    @PreAuthorize("hasAuthority('SuperAdmin')")
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


   @PreAuthorize("hasAuthority('User')")
   @PostMapping("/bookFutsal")
    public ResponseEntity<ApiResponse> bookFutsal(@RequestBody BookingInfo bookingInfo) {
        try {
            futsalService.bookFutsal(bookingInfo);
            Futsal futsal=futsalService.getFutsalByFutsalName(bookingInfo.getFutsalName());
            List<String> availableTimeList=futsal.getAvailableTimeList();
            List<String> bookingTimeList=bookingInfo.getBookingTimeList();
            availableTimeList.removeIf(bookingTimeList::contains);
            System.out.println("a="+availableTimeList);
            System.out.println("b="+bookingTimeList);
            futsal.setAvailableTimeList(availableTimeList);
            futsalService.saveFutsal(futsal);
            System.out.println("hello");
            long phone=futsal.getPhone();
            List<AppUser> user=userService.getUserByPhoneNumber(phone);
            List<String> futsalDeviceToken=user.get(0).getFutsalDeviceToken();

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

    @PutMapping("/acceptBookingInfo")
    public ResponseEntity<ApiResponse> acceptBookingInfo(@Valid @RequestBody BookingInfo bookingInfo) {

        try {
            List<AppUser> user=userService.getUserByPhoneNumber(bookingInfo.getContact());
            List<String> futsalDeviceToken=user.get(0).getFutsalDeviceToken();
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

                futsalService.updateBookingInfoStatus(bookingInfo);

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

    @GetMapping("/bookingInfoAccToFutsalName/{futsalName}")
    public ResponseEntity<ApiResponse> bookingInfoAccToFutsalName(@PathVariable("futsalName") String futsalName) {

        try {
            List<BookingInfo> bookingInfoList=futsalService.getBookingInfo(futsalName);

            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), bookingInfoList);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }



    @PreAuthorize("hasAuthority('Admin') OR hasAuthority('User')")
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



    @PreAuthorize("hasAuthority('Admin') OR hasAuthority('User')")
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

