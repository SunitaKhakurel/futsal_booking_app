package com.namus.futsalbookingsystem.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Entity
public class Futsal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private  int id;

    @NotBlank(message = "Futsal name cannot be blank")
    private String futsalName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
   private String email;

    @NotBlank(message = "Address cannot be blank")
   private String address;

    @Column(unique = true)
    @NotNull(message = "Phone cannot be null")
    @Positive(message = "Phone must be a positive number")
   private long phone;

    @NotBlank(message = "Time cannot be blank")
  private String time;

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }

    @Positive(message = "Price must be a positive number")
    @NotBlank(message = "Price cannot be blank")
    private float price;

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    @ElementCollection
    private List<String> image;

    @NotBlank(message = "Service cannot be blank")
    @ElementCollection
   private List<String> service;

    @NotBlank(message = "About Futsal cannot be blank")
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
