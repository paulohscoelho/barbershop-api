package com.barbershop.api.domain.controllers;

import java.util.List;

import com.barbershop.api.domain.entities.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.barbershop.api.domain.dtos.AppointmentRequestDTO;
import com.barbershop.api.domain.dtos.AppointmentResponseDTO;
import com.barbershop.api.domain.services.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

   private final AppointmentService appointmentService;

   @PostMapping
   public ResponseEntity<AppointmentResponseDTO> createAppointment(
       @RequestBody @Valid AppointmentRequestDTO dto,
       @AuthenticationPrincipal User loggedUser) {
      AppointmentResponseDTO response = appointmentService.createAppointment(dto, loggedUser);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);   }

   @GetMapping("/client/{clientId}")
   public ResponseEntity<List<AppointmentResponseDTO>> listByClient(@PathVariable UUID clientId) {
      return ResponseEntity.ok(appointmentService.listAppointmentsByClient(clientId));
   }

   @PatchMapping("/{id}/cancel")
   public ResponseEntity<AppointmentResponseDTO> cancel(@PathVariable UUID id) {
      return ResponseEntity.ok(appointmentService.cancelAppointment(id));
   }

   @PatchMapping("/{id}/complete")
   public ResponseEntity<AppointmentResponseDTO> complete(@PathVariable UUID id) {
      return ResponseEntity.ok(appointmentService.completeAppointment(id));
   }
}