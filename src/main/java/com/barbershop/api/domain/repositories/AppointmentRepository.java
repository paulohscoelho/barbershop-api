package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.Appointment;
import com.barbershop.api.domain.entities.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
   @Query("""
        SELECT COUNT(a) > 0 FROM Appointment a 
        WHERE a.barber.id = :barberId 
          AND a.startTime < :endTime 
          AND a.endTime > :startTime 
          AND a.status != :status
    """)
   boolean hasConflictingAppointment(
       @Param("barberId") UUID barberId,
       @Param("startTime") Instant startTime,
       @Param("endTime") Instant endTime,
       @Param("status") AppointmentStatus status
   );

   List<Appointment> findByClientId(UUID clientId);

   List<Appointment> findByBarberIdAndStartTimeBetween(
       UUID barberId,
       Instant startDate,
       Instant endDate
   );

}
