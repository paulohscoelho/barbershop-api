package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.ScheduleBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ScheduleBlockRepository extends JpaRepository<ScheduleBlock, UUID> {
   @Query("""
        SELECT COUNT(s) > 0 FROM ScheduleBlock s 
        WHERE s.barber.id = :barberId 
          AND s.startTime < :endTime 
          AND s.endTime > :startTime
    """)
   boolean hasBlockageInInterval(
       @Param("barberId") UUID barberId,
       @Param("startTime") Instant startTime,
       @Param("endTime") Instant endTime
   );

   List<ScheduleBlock> findByBarberId(UUID barberId);
}