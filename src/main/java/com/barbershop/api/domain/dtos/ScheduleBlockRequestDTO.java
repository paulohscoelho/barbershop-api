package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ScheduleBlockRequestDTO(
    @NotNull(message = "A data de início é obrigatória")
    @Future(message = "A data de início deve ser no futuro")
    Instant startTime,

    @NotNull(message = "A data de término é obrigatória")
    Instant endTime,

    @NotNull(message = "O motivo é obrigatório")
    String reason
) {}