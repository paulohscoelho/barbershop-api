package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.WorkingHourRequestDTO;
import com.barbershop.api.domain.dtos.WorkingHourResponseDTO;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.entities.WorkingHour;
import com.barbershop.api.domain.exceptions.RegraNegocioException;
import com.barbershop.api.domain.mappers.WorkingHourMapper;
import com.barbershop.api.domain.repositories.WorkingHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingHourService {

   private final WorkingHourRepository repository;
   private final WorkingHourMapper mapper;

   @Transactional
   public List<WorkingHourResponseDTO> updateWorkingHours(User barber, List<WorkingHourRequestDTO> dtos) {
      if (barber.getRole() != com.barbershop.api.domain.entities.UserRole.BARBER && barber.getRole() != com.barbershop.api.domain.entities.UserRole.ADMIN) {
         throw new RegraNegocioException("Apenas barbeiros ou admins podem configurar a agenda.");
      }

      for (WorkingHourRequestDTO dto : dtos) {
         if (dto.startTime().isAfter(dto.endTime()) || dto.startTime().equals(dto.endTime())) {
            throw new RegraNegocioException("O horário de início deve ser anterior ao horário de término.");
         }
      }

      repository.deleteByBarberId(barber.getId());

      List<WorkingHour> newHours = dtos.stream()
          .map(dto -> mapper.toEntity(dto, barber))
          .toList();

      List<WorkingHour> savedHours = repository.saveAll(newHours);

      return savedHours.stream()
          .map(mapper::toResponse)
          .toList();
   }

   @Transactional(readOnly = true)
   public List<WorkingHourResponseDTO> listWorkingHours(User barber) {
      return repository.findByBarberId(barber.getId())
          .stream()
          .map(mapper::toResponse)
          .toList();
   }
}