package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;
public record AppointmentRequestDTO(
    @NotNull(message = "O ID do barbeiro é obrigatório")
    UUID barberId,

    @NotNull(message = "O ID do serviço é obrigatório")
    UUID serviceId,

    @NotNull(message = "A data/hora de início é obrigatória")
    @Future(message = "A data/hora de início deve ser no futuro")
    Instant startTime
) {}