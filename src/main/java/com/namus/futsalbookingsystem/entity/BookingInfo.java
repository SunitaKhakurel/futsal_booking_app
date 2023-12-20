package com.namus.futsalbookingsystem.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
public class BookingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Contact cannot be null")
    @Positive(message = "Contact must be a positive number")
    private long contact;

    @NotNull(message = "Number of players cannot be null")
    @Min(value = 1, message = "Number of players must be at least 1")
    private int noOfPlayers;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "futsalName cannot be blank")
    private String futsalName;

    @NotBlank
    private String messageBody;

    @NotBlank
    private String title;

    @NotBlank
    private String time;

    @NotBlank
    private String venueCharge;

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenueCharge() {
        return venueCharge;
    }

    public void setVenueCharge(String venueCharge) {
        this.venueCharge = venueCharge;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @NotBlank
    private String paymentStatus;

    public String getFutsalName() {
        return futsalName;
    }

    public void setFutsalName(String futsalName) {
        this.futsalName = futsalName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getContact() {
        return contact;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public int getNoOfPlayers() {
        return noOfPlayers;
    }

    public void setNoOfPlayers(int noOfPlayers) {
        this.noOfPlayers = noOfPlayers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
