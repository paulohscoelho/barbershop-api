package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.BarberServiceRequestDTO;
import com.barbershop.api.domain.dtos.BarberServiceResponseDTO;
import com.barbershop.api.domain.entities.BarberService;
import com.barbershop.api.domain.mappers.BarberServiceMapper;
import com.barbershop.api.domain.repositories.BarberServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
