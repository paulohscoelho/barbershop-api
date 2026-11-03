package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO(
    @NotBlank(message = "A senha antiga é obrigatória")
    String oldPassword,

    @NotBlank(message = "A nova senha é obrigatória")
    @Size(min = 8, max = 100, message = "A nova senha deve ter no mínimo 8 caracteres")
    String newPassword
) {}