package com.barbershop.api.domain.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record BarberServiceResponseDTO(
    UUID id,
    String name,
    String description,
    Integer durationMinutes,
    BigDecimal price,
    Boolean active
) {}