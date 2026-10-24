package com.barbershop.api.domain.mappers;

import com.barbershop.api.domain.dtos.UserRequestDTO;
import com.barbershop.api.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

   @Mapping(target = "id", ignore = true)
   @Mapping(target = "createdAt", ignore = true)
   @Mapping(target = "role", constant = "CLIENT")
   @Mapping(target = "loyaltyCount", constant = "0")
   @Mapping(target = "passwordHash", source = "password")
   User toEntity(UserRequestDTO dto);
}