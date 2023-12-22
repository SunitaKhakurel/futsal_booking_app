package com.namus.futsalbookingsystem.controller;

import com.namus.futsalbookingsystem.entity.Events;
import com.namus.futsalbookingsystem.response.ApiResponse;
import com.namus.futsalbookingsystem.service.FutsalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Validated
public class EventController {

    @Autowired
    FutsalService futsalService;


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

}
