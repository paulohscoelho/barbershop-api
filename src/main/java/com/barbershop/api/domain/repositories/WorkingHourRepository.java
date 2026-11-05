package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkingHourRepository extends JpaRepository<WorkingHour, UUID> {
   List<WorkingHour> findByBarberId(UUID barberId);
   void deleteByBarberId(UUID barberId);
   List<WorkingHour> findByBarberIdAndDayOfWeek(UUID barberId, Integer dayOfWeek);
}