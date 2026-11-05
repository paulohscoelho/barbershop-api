package com.barbershop.api.domain.controllers;

import com.barbershop.api.domain.dtos.SpecialScheduleRequestDTO;
import com.barbershop.api.domain.dtos.SpecialScheduleResponseDTO;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.services.SpecialScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/special-schedules")
@RequiredArgsConstructor
public class SpecialScheduleController {

   private final SpecialScheduleService service;

   @PostMapping
   public ResponseEntity<SpecialScheduleResponseDTO> create(@RequestBody @Valid SpecialScheduleRequestDTO dto, @AuthenticationPrincipal User loggedUser) {
      return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto, loggedUser));
   }

   @GetMapping
   public ResponseEntity<List<SpecialScheduleResponseDTO>> list(@AuthenticationPrincipal User loggedUser) {
      return ResponseEntity.ok(service.list(loggedUser));
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable UUID id) {
      service.delete(id);
      return ResponseEntity.noContent().build();
   }
}