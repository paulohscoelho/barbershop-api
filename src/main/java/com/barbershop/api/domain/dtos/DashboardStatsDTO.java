package com.barbershop.api.domain.dtos;

public record DashboardStatsDTO(
    Long totalAgendamentos,
    Long agendados,
    Long finalizados,
    Long cancelados
) {}
