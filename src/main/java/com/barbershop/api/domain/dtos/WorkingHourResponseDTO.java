package com.barbershop.api.domain.dtos;

import java.time.LocalTime;
import java.util.UUID;

public record WorkingHourResponseDTO(
    UUID id,
    Integer dayOfWeek,
    LocalTime startTime,
    LocalTime endTime
) {}