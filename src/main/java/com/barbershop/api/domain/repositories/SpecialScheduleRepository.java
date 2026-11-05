package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.SpecialSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecialScheduleRepository extends JpaRepository<SpecialSchedule, UUID> {
   List<SpecialSchedule> findByBarberIdOrderByStartDateAsc(UUID barberId);

   @Query("""
        SELECT s FROM SpecialSchedule s 
        WHERE s.barber.id = :barberId 
          AND s.startDate <= :date 
          AND s.endDate >= :date
    """)
   Optional<SpecialSchedule> findByBarberIdAndDateRange(@Param("barberId") UUID barberId, @Param("date") LocalDate date);
}