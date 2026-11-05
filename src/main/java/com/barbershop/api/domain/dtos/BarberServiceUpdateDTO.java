package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record BarberServiceUpdateDTO(
    @NotBlank(message = "Nome é obrigatório") String name,
    String description,

    @NotNull(message = "Duração é obrigatória")
    @Min(10)
    Integer durationMinutes,

    @NotNull(message = "Preço é obrigatório")
    @Positive
    BigDecimal price,

    @NotNull
    Integer sortOrder
) {}