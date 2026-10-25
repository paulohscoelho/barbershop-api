package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.ScheduleBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ScheduleBlockRepository extends JpaRepository<ScheduleBlock, UUID> {}