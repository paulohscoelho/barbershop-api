package com.barbershop.api.domain.controllers;

import com.barbershop.api.domain.dtos.ScheduleBlockRequestDTO;
import com.barbershop.api.domain.dtos.ScheduleBlockResponseDTO;
import com.barbershop.api.domain.services.ScheduleBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedule-blocks")
@RequiredArgsConstructor
public class ScheduleBlockController {
   private final ScheduleBlockService service;

   @PostMapping
   public ResponseEntity<ScheduleBlockResponseDTO> create(@RequestBody @Valid ScheduleBlockRequestDTO dto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(service.createBlock(dto));
   }

   @GetMapping("/barber/{barberId}")
   public ResponseEntity<List<ScheduleBlockResponseDTO>> listByBarber(@PathVariable UUID barberId) {
      return ResponseEntity.ok(service.listBlocksByBarber(barberId));
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable UUID id) {
      service.deleteBlock(id);
      return ResponseEntity.noContent().build();
   }
}
