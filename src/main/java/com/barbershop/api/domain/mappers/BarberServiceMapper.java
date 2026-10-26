package com.barbershop.api.domain.mappers;

import com.barbershop.api.domain.dtos.BarberServiceRequestDTO;
import com.barbershop.api.domain.dtos.BarberServiceResponseDTO;
import com.barbershop.api.domain.entities.BarberService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BarberServiceMapper {
   @Mapping(target = "id", ignore = true)
   @Mapping(target = "active", constant = "true")
   BarberService toEntity(BarberServiceRequestDTO dto);

   BarberServiceResponseDTO toResponse(BarberService service);
}
