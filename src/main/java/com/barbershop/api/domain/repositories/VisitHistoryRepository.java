package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.VisitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory, UUID> {}
