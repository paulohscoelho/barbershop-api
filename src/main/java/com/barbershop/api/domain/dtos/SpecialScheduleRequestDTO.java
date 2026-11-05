package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record SpecialScheduleRequestDTO(
    @NotNull(message = "A data de início é obrigatória")
    LocalDate startDate,

    @NotNull(message = "A data de fim é obrigatória")
    LocalDate endDate,

    @NotNull(message = "Informe se está fechado")
    Boolean isClosed,

    LocalTime startTime,
    LocalTime endTime,
    String description
) {
    @AssertTrue(message = "A data de início deve ser anterior ou igual à data de fim")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) return true;
        return !startDate.isAfter(endDate);
    }
}