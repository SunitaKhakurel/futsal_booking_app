package com.namus.futsalbookingsystem.entity;

import javax.persistence.*;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Entity
public class Futsal {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  int id;
   private String futsalName;
   private String email;
   private String address;
    @Column(unique = true)
   private long phone;
  private String time;

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }

    private float price;

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    @ElementCollection
    private List<String> image;

    @ElementCollection
   private List<String> service;
   private String aboutFutsal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFutsalName() {
        return futsalName;
    }

    public void setFutsalName(String futsalName) {
        this.futsalName = futsalName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }




    public String getAboutFutsal() {
        return aboutFutsal;
    }

    public void setAboutFutsal(String aboutFutsal) {
        this.aboutFutsal = aboutFutsal;
    }
}
