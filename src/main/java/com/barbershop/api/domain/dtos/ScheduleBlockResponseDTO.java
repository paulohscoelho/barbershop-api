package com.barbershop.api.domain.dtos;

import java.time.Instant;
import java.util.UUID;

public record ScheduleBlockResponseDTO(
    UUID id, UUID barberId, Instant startTime, Instant endTime, String reason
) {}
