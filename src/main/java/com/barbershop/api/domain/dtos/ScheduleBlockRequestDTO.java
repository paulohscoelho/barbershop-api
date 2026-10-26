package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ScheduleBlockRequestDTO(
    @NotNull UUID barberId,
    @NotNull @Future Instant startTime,
    @NotNull @Future Instant endTime,
    @NotBlank String reason
) {}
