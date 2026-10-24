package com.barbershop.api.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record UserRequestDTO(

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
    String name,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Digite um email válido")
    @Size(max = 255, message = "O email não pode passar de 255 caracteres")
    String email,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 100, message = "A senha deve ter no mínimo 8 caracteres")
    String password,

    @NotBlank(message = "O telefone é obrigatório")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    String phone

) {
}