package com.barbershop.api.domain.dtos;

import com.barbershop.api.domain.entities.UserRole;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String name,
    String email,
    String phone,
    UserRole role,
    Integer loyaltyCount
) {
}