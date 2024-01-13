package com.namus.futsalbookingsystem.repository;

import com.namus.futsalbookingsystem.entity.Futsal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FutsalRepository extends JpaRepository<Futsal,Integer> {
Optional<Futsal>  findByPhone(long phone);
void deleteByPhone(long phone);

Optional<Futsal> findByFutsalName(String futsalName);

}
