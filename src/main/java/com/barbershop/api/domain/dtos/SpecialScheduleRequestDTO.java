package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record SpecialScheduleRequestDTO(
    @NotNull(message = "A data é obrigatória")
    LocalDate exceptionDate,
    @NotNull(message = "Informe se está fechado")
    Boolean isClosed,
    LocalTime startTime,
    LocalTime endTime,
    String description
) {}