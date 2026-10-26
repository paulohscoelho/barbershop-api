package com.barbershop.api.domain.controllers;

import com.barbershop.api.domain.dtos.ScheduleBlockRequestDTO;
import com.barbershop.api.domain.dtos.ScheduleBlockResponseDTO;
import com.barbershop.api.domain.services.ScheduleBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule-blocks")
@RequiredArgsConstructor
public class ScheduleBlockController {
   private final ScheduleBlockService service;

   @PostMapping
   public ResponseEntity<ScheduleBlockResponseDTO> create(@RequestBody @Valid ScheduleBlockRequestDTO dto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(service.createBlock(dto));
   }
}
