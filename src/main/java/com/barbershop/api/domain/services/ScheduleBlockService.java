package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.ScheduleBlockRequestDTO;
import com.barbershop.api.domain.dtos.ScheduleBlockResponseDTO;
import com.barbershop.api.domain.entities.ScheduleBlock;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.exceptions.RegraNegocioException;
import com.barbershop.api.domain.mappers.ScheduleBlockMapper;
import com.barbershop.api.domain.repositories.AppointmentRepository;
import com.barbershop.api.domain.repositories.ScheduleBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleBlockService {

   private final ScheduleBlockRepository blockRepository;
   private final AppointmentRepository appointmentRepository;
   private final ScheduleBlockMapper mapper;

   @Transactional
   public ScheduleBlockResponseDTO createBlock(ScheduleBlockRequestDTO dto, User barber) {
      if (dto.startTime().isAfter(dto.endTime()) || dto.startTime().equals(dto.endTime())) {
         throw new RegraNegocioException("A data de início deve ser anterior à data de término.");
      }

      if (barber.getRole() != com.barbershop.api.domain.entities.UserRole.BARBER && barber.getRole() != com.barbershop.api.domain.entities.UserRole.ADMIN) {
         throw new RegraNegocioException("Apenas barbeiros podem ter bloqueios de agenda.");
      }

      boolean hasAppointmentConflict = appointmentRepository.hasConflictingAppointment(
          barber.getId(), dto.startTime(), dto.endTime(), com.barbershop.api.domain.entities.AppointmentStatus.CANCELADO
      );
      if (hasAppointmentConflict) {
         throw new RegraNegocioException("Não é possível bloquear este horário, pois já existem agendamentos ativos.");
      }

      ScheduleBlock block = mapper.toEntity(dto, barber);
      return mapper.toResponse(blockRepository.save(block));
   }

   @Transactional(readOnly = true)
   public List<ScheduleBlockResponseDTO> listBlocksByBarber(User barber) {
      return blockRepository.findByBarberId(barber.getId())
          .stream()
          .map(mapper::toResponse)
          .toList();
   }

   @Transactional
   public void deleteBlock(UUID blockId) {
      ScheduleBlock block = blockRepository.findById(blockId)
          .orElseThrow(() -> new RegraNegocioException("Bloqueio não encontrado."));
      blockRepository.delete(block);
   }
}