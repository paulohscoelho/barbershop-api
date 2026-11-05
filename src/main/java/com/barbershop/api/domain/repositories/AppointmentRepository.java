package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.Appointment;
import com.barbershop.api.domain.entities.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

   List<Appointment> findByBarberIdAndStartTimeBetween(UUID barberId, Instant startDate, Instant endDate);

   Page<Appointment> findByClientId(UUID clientId, Pageable pageable);

   Long countByBarberId(UUID barberId);
   Long countByBarberIdAndStatus(UUID barberId, AppointmentStatus status);

   @Query("""
        SELECT a FROM Appointment a 
        WHERE a.barber.id = :barberId 
        AND (:status IS NULL OR a.status = :status)
        AND (COALESCE(:clientName, '') = '' OR LOWER(a.client.name) LIKE LOWER(CONCAT('%', :clientName, '%')))
    """)
   Page<Appointment> findByBarberAndFilters(
       @Param("barberId") UUID barberId,
       @Param("status") AppointmentStatus status,
       @Param("clientName") String clientName,
       Pageable pageable
   );
}