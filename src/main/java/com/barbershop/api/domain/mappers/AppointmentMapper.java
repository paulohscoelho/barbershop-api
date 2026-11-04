package com.barbershop.api.domain.mappers;

import com.barbershop.api.domain.dtos.AppointmentRequestDTO;
import com.barbershop.api.domain.dtos.AppointmentResponseDTO;
import com.barbershop.api.domain.entities.Appointment;
import com.barbershop.api.domain.entities.BarberService;
import com.barbershop.api.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

   @Mapping(source = "client.id", target = "clientId")
   @Mapping(source = "client.name", target = "clientName")
   @Mapping(source = "barber.id", target = "barberId")
   @Mapping(source = "barber.name", target = "barberName")
   @Mapping(source = "services", target = "serviceNames")
   AppointmentResponseDTO toResponse(Appointment appointment);

   @Mapping(target = "id", ignore = true)
   @Mapping(target = "createdAt", ignore = true)
   @Mapping(target = "status", constant = "AGENDADO")
   @Mapping(source = "dto.startTime", target = "startTime")
   @Mapping(source = "endTime", target = "endTime")
   @Mapping(source = "client", target = "client")
   @Mapping(source = "barber", target = "barber")
   @Mapping(source = "services", target = "services")
   Appointment toEntity(AppointmentRequestDTO dto, User client, User barber, List<BarberService> services, Instant endTime);

   default List<String> mapServicesToNames(List<BarberService> services) {
      if (services == null) {
         return List.of();
      }
      return services.stream().map(BarberService::getName).toList();
   }
}