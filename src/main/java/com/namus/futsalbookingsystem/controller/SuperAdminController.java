package com.namus.futsalbookingsystem.controller;

import com.namus.futsalbookingsystem.entity.AppUser;
import com.namus.futsalbookingsystem.entity.Futsal;
import com.namus.futsalbookingsystem.repository.FutsalRepository;
import com.namus.futsalbookingsystem.response.ApiResponse;
import com.namus.futsalbookingsystem.service.FutsalService;
import com.namus.futsalbookingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class SuperAdminController {
    @Autowired
    private UserService service;
    @Autowired
    FutsalService futsalService;



    @Autowired
    private FutsalRepository futsalRepository;



    @CrossOrigin
    @PostMapping("/getAdminDetailsForSup")
    public ResponseEntity<ApiResponse> listAllAdminForSup() {
        try {
            System.out.println("hello");
            List<AppUser> adminList = service.getAdminDetails();
            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), adminList);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }

    @CrossOrigin
    @PostMapping("/getFutsalDetailsForSuperAdmin")
    public ResponseEntity<ApiResponse> listAllFutsalForSup() {
        try {
            List<Futsal> futsalList = futsalService.getAllFutsalData();
            ApiResponse apiResponse = new ApiResponse("success", HttpStatus.OK.value(), futsalList);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }



}
