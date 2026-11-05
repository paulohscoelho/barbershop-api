package com.barbershop.api.domain.controllers;

import java.time.LocalDate;
import java.util.List;

import com.barbershop.api.domain.entities.User;
import org.springframework.format.annotation.DateTimeFormat;
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

   @GetMapping("/barber")
   public ResponseEntity<List<AppointmentResponseDTO>> listByBarber(@AuthenticationPrincipal User loggedUser) {
      return ResponseEntity.ok(appointmentService.listAppointmentsByBarber(loggedUser.getId()));
   }

   @PatchMapping("/{id}/cancel")
   public ResponseEntity<AppointmentResponseDTO> cancel(@PathVariable UUID id) {
      return ResponseEntity.ok(appointmentService.cancelAppointment(id));
   }

   @PatchMapping("/{id}/complete")
   public ResponseEntity<AppointmentResponseDTO> complete(@PathVariable UUID id) {
      return ResponseEntity.ok(appointmentService.completeAppointment(id));
   }

   @GetMapping("/available-slots")
   public ResponseEntity<List<String>> getAvailableSlots(
       @RequestParam UUID barberId,
       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
       @RequestParam List<UUID> serviceIds) {

      List<String> slots = appointmentService.getAvailableSlots(barberId, date, serviceIds);
      return ResponseEntity.ok(slots);
   }
}