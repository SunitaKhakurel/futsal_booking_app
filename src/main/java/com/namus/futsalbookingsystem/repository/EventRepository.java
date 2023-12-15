package com.namus.futsalbookingsystem.repository;

import com.namus.futsalbookingsystem.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Events,Integer> {
}
