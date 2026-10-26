package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BarberServiceRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String name,

    String description,

    @NotNull(message = "Duração é obrigatória")
    @Min(10)
    Integer durationMinutes,

    @NotNull(message = "Preço é obrigatório")
    @Positive
    BigDecimal price
) {}
