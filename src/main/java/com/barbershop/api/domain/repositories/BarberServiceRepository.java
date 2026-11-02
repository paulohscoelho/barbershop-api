package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.BarberService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BarberServiceRepository extends JpaRepository<BarberService, UUID> {
   List<BarberService> findAllByActiveTrue();
}
