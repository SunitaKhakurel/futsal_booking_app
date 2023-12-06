package com.namus.futsalbookingsystem.repository;


import com.namus.futsalbookingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByuserName(String username);
    List<User> findByPhone(Long phone);
    List<User> findByRole(String role);
}

