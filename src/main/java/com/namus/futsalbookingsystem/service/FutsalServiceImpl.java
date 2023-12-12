package com.namus.futsalbookingsystem.service;

import com.namus.futsalbookingsystem.entity.Futsal;
import com.namus.futsalbookingsystem.repository.FutsalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FutsalServiceImpl implements FutsalService {
    @Autowired
    FutsalRepository futsalRepository;
    @Override
    public void saveFutsal(Futsal futsal) {

        futsalRepository.save(futsal);

    }

    @Override
    public List<Futsal> getAllFutsalData() {

        return futsalRepository.findAll();
    }

    @Override
    public Futsal getFutsalByPhoneNumber(long phone) {
        Optional<Futsal> futsal=futsalRepository.findByPhone(phone);
        return futsal.orElse(null);
    }

    @Override
    public void updateFutsalDetails(Futsal futsal, long phone) {
       Futsal futsal1=getFutsalByPhoneNumber(phone);
       if(futsal1!=null){
           futsal1.setFutsalName(futsal.getFutsalName());
           futsal1.setEmail(futsal.getEmail());
           futsal1.setTime(futsal.getTime());
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
}
