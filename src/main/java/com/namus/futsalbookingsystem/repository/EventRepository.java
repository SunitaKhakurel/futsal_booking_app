package com.namus.futsalbookingsystem.repository;

import com.namus.futsalbookingsystem.entity.AppUser;
import com.namus.futsalbookingsystem.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EventRepository extends JpaRepository<Events,Integer> {
    List<Events> findByFutsalName(String futsalName);
}
