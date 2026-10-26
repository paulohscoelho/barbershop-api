package com.barbershop.api.domain.controllers;

import com.barbershop.api.domain.dtos.BarberServiceRequestDTO;
import com.barbershop.api.domain.dtos.BarberServiceResponseDTO;
import com.barbershop.api.domain.services.BarberServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class BarberServiceController {
   private final BarberServiceService service;

   @PostMapping
   public ResponseEntity<BarberServiceResponseDTO> create(@RequestBody @Valid BarberServiceRequestDTO dto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
   }
}