package com.namus.futsalbookingsystem.service;

import com.namus.futsalbookingsystem.entity.BookingInfo;
import com.namus.futsalbookingsystem.entity.Events;
import com.namus.futsalbookingsystem.entity.Futsal;
import com.namus.futsalbookingsystem.entity.RegisterTeam;

import java.util.List;
import java.util.Optional;

public interface FutsalService {
    public void  saveFutsal(Futsal futsal);
    public List<Futsal> getAllFutsalData();

    public Futsal getFutsalByPhoneNumber(long phone);
    public Futsal getFutsalByFutsalName(String futsalName);
    public void updateFutsalDetails(Futsal futsal,long phone);
    public void deleteFutsal(long phone);

    public void addEvent(Events event);
    public List<Events> eventsDetails();
    public void updateEventDetails(Events events,int id);
    public Events getEventsById(int id);
    public void deleteEvent(int id);
    public void registerTeam(RegisterTeam registerTeam);
    public List<Events> getEventAccordingToFutsalName(String futsalName);
    public List<RegisterTeam> getregInfoByFutsalName(String futsalName);

    public void bookFutsal(BookingInfo bookingInfo);
    public List<BookingInfo> getBookingInfo(String futsalName);

    public List<BookingInfo> getAcceptedBookingInfo(String futsalName);

    public List<BookingInfo> getAcceptedBookingInfoAccphoneno(Long phone);

    public void updateBookingInfoStatus(BookingInfo bookingInfo);
}



