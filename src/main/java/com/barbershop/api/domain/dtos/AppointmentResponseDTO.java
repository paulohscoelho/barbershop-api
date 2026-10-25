package com.barbershop.api.domain.dtos;

import com.barbershop.api.domain.entities.AppointmentStatus;
import java.time.Instant;
import java.util.UUID;

public record AppointmentResponseDTO(
    UUID id,
    UUID clientId,
    String clientName,
    UUID barberId,
    String barberName,
    UUID serviceId,
    String serviceName,
    Instant startTime,
    Instant endTime,
    AppointmentStatus status
) {}
