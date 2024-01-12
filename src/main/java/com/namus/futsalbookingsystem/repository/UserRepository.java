package com.namus.futsalbookingsystem.repository;


import com.namus.futsalbookingsystem.App;
import com.namus.futsalbookingsystem.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<AppUser,Integer> {
    Optional<AppUser> findByuserName(String username);

    AppUser findByUserName(String username);

    List<AppUser> findByPhone(Long phone);
    List<AppUser> findByRole(String role);

    Optional<AppUser> findByEmail(String email);



}

