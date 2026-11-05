package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.SpecialScheduleRequestDTO;
import com.barbershop.api.domain.dtos.SpecialScheduleResponseDTO;
import com.barbershop.api.domain.entities.SpecialSchedule;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.exceptions.RegraNegocioException;
import com.barbershop.api.domain.repositories.SpecialScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecialScheduleService {

   private final SpecialScheduleRepository repository;

   @Transactional
   public SpecialScheduleResponseDTO create(SpecialScheduleRequestDTO dto, User barber) {
      if (dto.startDate().isAfter(dto.endDate())) {
         throw new RegraNegocioException("A data de início deve ser anterior à data de fim.");
      }

      if (!dto.isClosed()) {
         if (dto.startTime() == null || dto.endTime() == null) {
            throw new RegraNegocioException("Horários de início e fim são obrigatórios para dias abertos.");
         }
         if (dto.startTime().isAfter(dto.endTime())) {
            throw new RegraNegocioException("O horário de início deve ser anterior ao de término.");
         }
      }

      SpecialSchedule special = new SpecialSchedule();
      special.setBarber(barber);
      special.setStartDate(dto.startDate());
      special.setEndDate(dto.endDate());
      special.setIsClosed(dto.isClosed());
      special.setStartTime(dto.isClosed() ? null : dto.startTime());
      special.setEndTime(dto.isClosed() ? null : dto.endTime());
      special.setDescription(dto.description());

      return mapToResponse(repository.save(special));
   }

   @Transactional(readOnly = true)
   public List<SpecialScheduleResponseDTO> list(User barber) {
      return repository.findByBarberIdOrderByStartDateAsc(barber.getId())
          .stream().map(this::mapToResponse).toList();
   }

   @Transactional
   public void delete(UUID id) {
      repository.deleteById(id);
   }

   private SpecialScheduleResponseDTO mapToResponse(SpecialSchedule s) {
      return new SpecialScheduleResponseDTO(
          s.getId(),
          s.getStartDate(),
          s.getEndDate(),
          s.getIsClosed(),
          s.getStartTime(),
          s.getEndTime(),
          s.getDescription()
      );
   }
}