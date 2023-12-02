package com.namus.futsalbookingsystem.repository;

import com.namus.futsalbookingsystem.entity.Futsal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FutsalRepository extends JpaRepository<Futsal,Integer> {
}
