package com.namus.futsalbookingsystem.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
public class RegisterTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Team Name cannot be blank")
    @Size(min = 2, max = 100, message = "Team Name must be between 2 and 100 characters")
    private String teamName;

    @NotNull(message = "Contact cannot be null")
    @Positive(message = "Contact must be a positive number")
    private String contact;

    @NotNull(message = "Number of players cannot be null")
    @Min(value = 1, message = "Number of players must be at least 1")
    private int noOfPlayers;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Event Title cannot be blank")
    private String eventTitle;

    @NotBlank(message = "Futsal Name cannot be blank")
    private String futsalName;

    public String getFutsalName() {
        return futsalName;
    }

    public void setFutsalName(String futsalName) {
        this.futsalName = futsalName;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
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
