package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileDTO(
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
    String name,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Digite um email válido")
    String email,

    @NotBlank(message = "O telefone é obrigatório")
    String phone
) {}