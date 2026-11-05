package com.barbershop.api.domain.mappers;

import com.barbershop.api.domain.dtos.WorkingHourRequestDTO;
import com.barbershop.api.domain.dtos.WorkingHourResponseDTO;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.entities.WorkingHour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkingHourMapper {

   @Mapping(target = "id", ignore = true)
   @Mapping(target = "barber", source = "barber")
   @Mapping(target = "active", constant = "true")
   WorkingHour toEntity(WorkingHourRequestDTO dto, User barber);

   WorkingHourResponseDTO toResponse(WorkingHour workingHour);
}