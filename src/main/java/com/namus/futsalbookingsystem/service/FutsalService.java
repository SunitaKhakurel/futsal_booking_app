package com.namus.futsalbookingsystem.service;

import com.namus.futsalbookingsystem.entity.BookingInfo;
import com.namus.futsalbookingsystem.entity.Events;
import com.namus.futsalbookingsystem.entity.Futsal;
import com.namus.futsalbookingsystem.entity.RegisterTeam;

import java.util.List;

public interface FutsalService {
    public void  saveFutsal(Futsal futsal);
    public List<Futsal> getAllFutsalData();

    public Futsal getFutsalByPhoneNumber(long phone);

    public void updateFutsalDetails(Futsal futsal,long phone);
    public void deleteFutsal(long phone);

    public void addEvent(Events event);
    public List<Events> eventsDetails();
    public void updateEventDetails(Events events,int id);
    public Events getEventsById(int id);
    public void deleteEvent(int id);
    public void registerTeam(RegisterTeam registerTeam);
    public void bookFutsal(BookingInfo bookingInfo);
}



