package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.SpecialSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecialScheduleRepository extends JpaRepository<SpecialSchedule, UUID> {
   List<SpecialSchedule> findByBarberIdOrderByExceptionDateAsc(UUID barberId);
   Optional<SpecialSchedule> findByBarberIdAndExceptionDate(UUID barberId, LocalDate exceptionDate);
}