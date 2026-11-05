package com.barbershop.api.domain.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record SpecialScheduleResponseDTO(
    UUID id,
    LocalDate startDate,
    LocalDate endDate,
    Boolean isClosed,
    LocalTime startTime,
    LocalTime endTime,
    String description
) {}