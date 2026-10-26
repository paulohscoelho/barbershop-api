package com.barbershop.api.domain.mappers;

import com.barbershop.api.domain.dtos.ScheduleBlockRequestDTO;
import com.barbershop.api.domain.dtos.ScheduleBlockResponseDTO;
import com.barbershop.api.domain.entities.ScheduleBlock;
import com.barbershop.api.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleBlockMapper {
   @Mapping(target = "id", ignore = true)
   @Mapping(source = "barber", target = "barber")
   ScheduleBlock toEntity(ScheduleBlockRequestDTO dto, User barber);

   @Mapping(source = "barber.id", target = "barberId")
   ScheduleBlockResponseDTO toResponse(ScheduleBlock block);
}
