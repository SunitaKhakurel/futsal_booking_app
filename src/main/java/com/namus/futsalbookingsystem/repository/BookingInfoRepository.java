package com.namus.futsalbookingsystem.repository;

import com.namus.futsalbookingsystem.entity.BookingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingInfoRepository extends JpaRepository<BookingInfo,Integer> {
}
