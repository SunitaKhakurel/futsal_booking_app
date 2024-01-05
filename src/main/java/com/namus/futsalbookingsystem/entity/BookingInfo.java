package com.namus.futsalbookingsystem.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
public class BookingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

    private String name;

    private long contact;


    private int noOfPlayers;


    private String address;


    private String futsalName;


    private String messageBody;


    private String title;





    private String venueCharge;

    public List<String> getBookingTimeList() {
        return bookingTimeList;
    }

    public void setBookingTimeList(List<String> bookingTimeList) {
        this.bookingTimeList = bookingTimeList;
    }

    @ElementCollection
    private List<String> bookingTimeList;

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
