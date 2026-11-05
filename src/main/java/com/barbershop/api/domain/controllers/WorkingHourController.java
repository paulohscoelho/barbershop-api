package com.barbershop.api.domain.controllers;

import com.barbershop.api.domain.dtos.WorkingHourRequestDTO;
import com.barbershop.api.domain.dtos.WorkingHourResponseDTO;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.services.WorkingHourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/working-hours")
@RequiredArgsConstructor
public class WorkingHourController {

   private final WorkingHourService service;

   @PostMapping
   public ResponseEntity<List<WorkingHourResponseDTO>> updateWorkingHours(
       @RequestBody @Valid List<WorkingHourRequestDTO> dtos,
       @AuthenticationPrincipal User loggedUser) {

      return ResponseEntity.ok(service.updateWorkingHours(loggedUser, dtos));
   }

   @GetMapping
   public ResponseEntity<List<WorkingHourResponseDTO>> listWorkingHours(@AuthenticationPrincipal User loggedUser) {
      return ResponseEntity.ok(service.listWorkingHours(loggedUser));
   }
}