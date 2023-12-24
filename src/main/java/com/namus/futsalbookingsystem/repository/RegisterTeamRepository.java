package com.namus.futsalbookingsystem.repository;

import com.namus.futsalbookingsystem.entity.RegisterTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterTeamRepository extends JpaRepository<RegisterTeam,Integer> {
List<RegisterTeam> findByFutsalName(String futsalName);
}
