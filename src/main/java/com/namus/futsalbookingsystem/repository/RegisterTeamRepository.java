package com.namus.futsalbookingsystem.repository;

import com.namus.futsalbookingsystem.entity.RegisterTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegisterTeamRepository extends JpaRepository<RegisterTeam,Integer> {
List<RegisterTeam> findByFutsalName(String futsalName);
}
