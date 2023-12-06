package com.namus.futsalbookingsystem.repository;

import com.namus.futsalbookingsystem.entity.Futsal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FutsalRepository extends JpaRepository<Futsal,Integer> {

}
