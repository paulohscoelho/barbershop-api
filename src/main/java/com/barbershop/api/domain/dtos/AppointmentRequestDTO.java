package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
public record AppointmentRequestDTO(
    @NotNull(message = "O ID do barbeiro é obrigatório")
    UUID barberId,

    @NotEmpty(message = "Selecione pelo menos um serviço")
    List<UUID> serviceIds,

    @NotNull(message = "A data/hora de início é obrigatória")
    @Future(message = "A data/hora de início deve ser no futuro")
    Instant startTime
) {}