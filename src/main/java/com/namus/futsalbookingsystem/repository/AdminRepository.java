package com.namus.futsalbookingsystem.repository;

import com.namus.futsalbookingsystem.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
        Optional<Admin> findByuserName(String username);

        List<Admin> findByPhone(Long phone);
        }
