package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.BarberServiceRequestDTO;
import com.barbershop.api.domain.dtos.BarberServiceResponseDTO;
import com.barbershop.api.domain.dtos.BarberServiceUpdateDTO;
import com.barbershop.api.domain.entities.BarberService;
import com.barbershop.api.domain.exceptions.RegraNegocioException;
import com.barbershop.api.domain.mappers.BarberServiceMapper;
import com.barbershop.api.domain.repositories.BarberServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarberServiceService {

   private final BarberServiceRepository repository;
   private final BarberServiceMapper mapper;

   @Transactional
   public BarberServiceResponseDTO create(BarberServiceRequestDTO dto) {
      BarberService service = mapper.toEntity(dto);
      return mapper.toResponse(repository.save(service));
   }

   @Transactional(readOnly = true)
   public List<BarberServiceResponseDTO> listActiveServices() {
      return repository.findAllByActiveTrue()
          .stream()
          .map(mapper::toResponse)
          .toList();
   }

   @Transactional(readOnly = true)
   public List<BarberServiceResponseDTO> listAllForAdmin() {
      return repository.findAllByOrderBySortOrderAsc()
          .stream()
          .map(mapper::toResponse)
          .toList();
   }

   @Transactional
   public BarberServiceResponseDTO update(UUID id, BarberServiceUpdateDTO dto) {
      BarberService service = repository.findById(id)
          .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado."));

      service.setName(dto.name());
      service.setDescription(dto.description());
      service.setDurationMinutes(dto.durationMinutes());
      service.setPrice(dto.price());
      service.setSortOrder(dto.sortOrder());

      return mapper.toResponse(repository.save(service));
   }

   @Transactional
   public void deactivateService(UUID id) {
      BarberService service = repository.findById(id)
          .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado."));

      if (!service.getActive()) {
         throw new RegraNegocioException("Este serviço já está inativo.");
      }

      service.setActive(false);
      repository.save(service);
   }
}