package com.namus.futsalbookingsystem.entity;

import javax.persistence.*;
import java.io.InputStream;

@Entity
public class Futsal {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  int id;
    private String futsalName;
    private String futsalDescription;
   private int price;

    @Lob
    private byte[] image;

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

    public String getFutsalDescription() {
        return futsalDescription;
    }

    public void setFutsalDescription(String futsalDescription) {
        this.futsalDescription = futsalDescription;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
