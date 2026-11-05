package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record WorkingHourRequestDTO(
    @NotNull(message = "O dia da semana é obrigatório")
    Integer dayOfWeek,

    @NotNull(message = "O horário de início é obrigatório")
    LocalTime startTime,

    @NotNull(message = "O horário de término é obrigatório")
    LocalTime endTime
) {}