package com.barbershop.api.domain.controllers;

import com.barbershop.api.domain.dtos.BarberServiceRequestDTO;
import com.barbershop.api.domain.dtos.BarberServiceResponseDTO;
import com.barbershop.api.domain.dtos.BarberServiceUpdateDTO;
import com.barbershop.api.domain.services.BarberServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class BarberServiceController {

   private final BarberServiceService service;

   @PostMapping
   public ResponseEntity<BarberServiceResponseDTO> create(@RequestBody @Valid BarberServiceRequestDTO dto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
   }

   @GetMapping
   public ResponseEntity<List<BarberServiceResponseDTO>> listActive() {
      return ResponseEntity.ok(service.listActiveServices());
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable UUID id) {
      service.deleteService(id);
      return ResponseEntity.noContent().build();
   }

   @GetMapping("/admin")
   public ResponseEntity<List<BarberServiceResponseDTO>> listAllAdmin(@RequestParam(defaultValue = "true") Boolean active) {
      return ResponseEntity.ok(service.listAllForAdmin(active));
   }

   @PutMapping("/{id}")
   public ResponseEntity<BarberServiceResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid BarberServiceUpdateDTO dto) {
      return ResponseEntity.ok(service.update(id, dto));
   }

   @PatchMapping("/{id}/deactivate")
   public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
      service.deactivateService(id);
      return ResponseEntity.noContent().build();
   }
}