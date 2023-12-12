package com.namus.futsalbookingsystem.service;

import com.namus.futsalbookingsystem.entity.Futsal;

import java.util.List;

public interface FutsalService {
    public void  saveFutsal(Futsal futsal);
    public List<Futsal> getAllFutsalData();

    public Futsal getFutsalByPhoneNumber(long phone);

    public void updateFutsalDetails(Futsal futsal,long phone);
    public void deleteFutsal(long phone);
}



