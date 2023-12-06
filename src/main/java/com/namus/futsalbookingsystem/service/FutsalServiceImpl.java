package com.namus.futsalbookingsystem.service;

import com.namus.futsalbookingsystem.entity.Futsal;
import com.namus.futsalbookingsystem.repository.FutsalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
