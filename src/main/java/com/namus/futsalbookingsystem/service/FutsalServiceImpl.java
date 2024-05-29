package com.namus.futsalbookingsystem.service;

import com.namus.futsalbookingsystem.entity.BookingInfo;
import com.namus.futsalbookingsystem.entity.Events;
import com.namus.futsalbookingsystem.entity.Futsal;
import com.namus.futsalbookingsystem.entity.RegisterTeam;
import com.namus.futsalbookingsystem.repository.BookingInfoRepository;
import com.namus.futsalbookingsystem.repository.EventRepository;
import com.namus.futsalbookingsystem.repository.FutsalRepository;
import com.namus.futsalbookingsystem.repository.RegisterTeamRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class FutsalServiceImpl implements FutsalService {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    @Autowired
    FutsalRepository futsalRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    BookingInfoRepository bookingInfoRepository;
    @Autowired
    RegisterTeamRepository registerTeamRepository;

    @Override
    public void saveFutsal(Futsal futsal) {

        futsalRepository.save(futsal);
    }

    @Override
    public List<Futsal> getAllFutsalData() {

        return futsalRepository.findAll();
    }

    @Override
    public void updateFutsalDetails(Futsal futsal, long phone) {
        Futsal futsal1=getFutsalByPhoneNumber(phone);
        if(futsal1!=null){
            futsal1.setFutsalName(futsal.getFutsalName());
            futsal1.setEmail(futsal.getEmail());
            futsal1.setOpeningTime(futsal.getOpeningTime());
            futsal1.setClosingTime(futsal.getClosingTime());
            futsal1.setAddress(futsal1.getAddress());
            futsal1.setPrice(futsal.getPrice());
            futsal1.setImage(futsal.getImage());
            futsal1.setService(futsal.getService());
            futsalRepository.save(futsal1);
        }
    }

    @Override
    public void deleteFutsal(long phone) {
        Futsal futsal=getFutsalByPhoneNumber(phone);

        futsalRepository.deleteById(futsal.getId());
    }


    @Override
    public Futsal getFutsalByPhoneNumber(long phone) {
        Optional<Futsal> futsal=futsalRepository.findByPhone(phone);
        return futsal.orElse(null);
    }

    @Override
    public Futsal getFutsalByFutsalName(String futsalName) {
        Optional<Futsal> futsal=futsalRepository.findByFutsalName(futsalName);
        return futsal.orElse(null);
    }





    @Override
    public void addEvent(Events event) {
        eventRepository.save(event);
    }

    @Override
    public List<Events> eventsDetails() {
        List<Events> events=eventRepository.findAll();
        return events;
    }

    @Override
    public void updateEventDetails(Events events, int id) {
        Events event=getEventsById(id);
        if(event!=null){
            event.setFutsalName(events.getFutsalName());
            event.setEventTitle(events.getEventTitle());
            event.setAddress(events.getAddress());
            event.setEventImage(events.getEventImage());
            eventRepository.save(event);
        }

    }

    @Override
    public Events getEventsById(int id) {
        Optional<Events> events=eventRepository.findById(id);
        return events.orElse(null);
    }

    @Override
    public void deleteEvent(int id) {
        Events event=getEventsById(id);
        eventRepository.deleteById(event.getId());
    }

    @Override
    public void registerTeam(RegisterTeam registerTeam) {
        registerTeamRepository.save(registerTeam);
    }

    @Override
    public List<Events> getEventAccordingToFutsalName(String futsalName) {
        List<Events> events=eventRepository.findByFutsalName(futsalName);
        return events;
    }

    @Override
    public List<RegisterTeam> getregInfoByFutsalName(String futsalName) {
        List<RegisterTeam> teams=registerTeamRepository.findByFutsalName(futsalName);
        return teams;
    }

    @Override
    public void bookFutsal(BookingInfo bookingInfo) {
        bookingInfoRepository.save(bookingInfo);
    }

    @Override
    public List<BookingInfo> getBookingInfo(String futsalName) {

        List<BookingInfo> bookingInfoList = bookingInfoRepository.getByFutsalName(futsalName);
                Collections.sort(bookingInfoList, Comparator.comparingInt(BookingInfo::getId).reversed());

        return bookingInfoList;
    }

    @Override
    public List<BookingInfo> getAcceptedBookingInfo(String futsalName) {
        List<BookingInfo> bookingInfoList = bookingInfoRepository.getByFutsalName(futsalName);
        List<BookingInfo> acceptedBookings = bookingInfoList.stream()
                .filter(bookingInfo -> "Accepted".equals(bookingInfo.getStatus())) // Assuming the status is a String
                .collect(Collectors.toList());

// Sort the filtered list by id in reverse order
        Collections.sort(acceptedBookings, Comparator.comparingInt(BookingInfo::getId).reversed());
        return acceptedBookings;
    }

    @Override
    public List<BookingInfo> getAcceptedBookingInfoAccphoneno(Long phone) {
        List<BookingInfo> bookingInfoList=bookingInfoRepository.getByContact(phone);

        List<BookingInfo> acceptedBookings = bookingInfoList.stream()
                .filter(bookingInfo -> "Accepted".equals(bookingInfo.getStatus()) ||
                        "Declined".equals(bookingInfo.getStatus())) // Assuming the status is a String
                .collect(Collectors.toList());

// Sort the filtered list by id in reverse order
        Collections.sort(acceptedBookings, Comparator.comparingInt(BookingInfo::getId).reversed());
        return acceptedBookings;
    }

    @Override
    public void updateAvailableTimeListAfterDecline(BookingInfo bookingInfo) {
        Optional<Futsal> futsals=futsalRepository.findByFutsalName(bookingInfo.getFutsalName());
        List<String> bookingTimelist=bookingInfo.getBookingTimeList();
        Futsal futsal=futsals.orElse(null);
        if(futsal!=null){
            List<String> availableTimeList=futsal.getAvailableTimeList();
            availableTimeList.addAll(bookingTimelist);
            Collections.sort(availableTimeList,new TimeStringComparator());
            futsal.setAvailableTimeList(availableTimeList);
            futsalRepository.save(futsal);
        }
    }

    static class TimeStringComparator implements Comparator<String> {
        @Override
        public int compare(String timeString1, String timeString2) {
            LocalTime localTime1 = parseTimeString(timeString1);
            LocalTime localTime2 = parseTimeString(timeString2);
            return localTime1.compareTo(localTime2);
        }

        private LocalTime parseTimeString(String timeString) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            return LocalTime.parse(timeString.toUpperCase(), formatter);
        }
    }

    @Override
    public void updateBookingInfoStatus(BookingInfo bookingInfo) {
        BookingInfo bookingInfo1=bookingInfoRepository.getById(bookingInfo.getId());
        if(bookingInfo1!=null){
            bookingInfo1.setStatus(bookingInfo.getStatus());
            bookingInfoRepository.save(bookingInfo1);
        }
    }

    @Override
    public List<BookingInfo> getAcceptedDeclinedBookingInfo(Long phone) {
        List<BookingInfo> bookingInfoList=bookingInfoRepository.getByContact(phone);
        List<BookingInfo> Bookings = bookingInfoList.stream()
                .filter(bookingInfo -> "Accepted".equals(bookingInfo.getStatus()) ||
                        "Pending".equals(bookingInfo.getStatus())) // Assuming the status is a String
                .collect(Collectors.toList());

// Sort the filtered list by id in reverse order
        Collections.sort(Bookings, Comparator.comparingInt(BookingInfo::getId).reversed());
        return Bookings;

    }

    @Override
    public boolean isTimeBefore(String timeString, String compareTime) {
        LocalTime time = LocalTime.parse(timeString, timeFormatter);
        LocalTime compareLocalTime = LocalTime.parse(compareTime, timeFormatter);
        return time.isBefore(compareLocalTime);
    }

    @Override
    public void deleteBookings(int id) {
        BookingInfo bookingInfo=bookingInfoRepository.getById(id);
        if(bookingInfo!=null) {
            bookingInfoRepository.deleteById(bookingInfo.getId());
        }
    }

    @Scheduled(cron = "0 45 10 * * *") // Run at midnight every day
    public void resetAvailableTimeList() {
        // Retrieve all Futsal entities
        List<Futsal> allFutsals = futsalRepository.findAll();
try {
    // Reset the availableTimeList for each Futsal
    for (Futsal futsal : allFutsals) {
        List<String> originalAvailableTimeList = futsal.getOriginalTimeList();

        originalAvailableTimeList.size(); // Force initialization

        // Reset the availableTimeList
        futsal.setAvailableTimeList(new ArrayList<>(originalAvailableTimeList));


        futsalRepository.save(futsal);

    }
}catch (Exception e){
    System.out.println(e);
}
    }

}
