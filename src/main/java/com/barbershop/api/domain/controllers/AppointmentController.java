package com.barbershop.api.domain.controllers;

import java.time.LocalDate;
import java.util.List;

import com.barbershop.api.domain.dtos.DashboardStatsDTO;
import com.barbershop.api.domain.entities.AppointmentStatus;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

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
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

   @GetMapping("/barber")
   public ResponseEntity<Page<AppointmentResponseDTO>> listByBarber(
       @AuthenticationPrincipal User loggedUser,
       @RequestParam(required = false) AppointmentStatus status,
       @RequestParam(required = false) String clientName,
       @PageableDefault(size = 10, sort = "startTime") Pageable pageable) {
      return ResponseEntity.ok(appointmentService.listAppointmentsByBarber(loggedUser.getId(), status, clientName, pageable));
   }

   @GetMapping("/barber/stats")
   public ResponseEntity<DashboardStatsDTO> getBarberStats(@AuthenticationPrincipal User loggedUser) {
      return ResponseEntity.ok(appointmentService.getDashboardStats(loggedUser.getId()));
   }

   @GetMapping("/client")
   public ResponseEntity<Page<AppointmentResponseDTO>> listByClient(
       @AuthenticationPrincipal User loggedUser,
       @PageableDefault(size = 10, sort = "startTime") Pageable pageable) {
      return ResponseEntity.ok(appointmentService.listAppointmentsByClient(loggedUser.getId(), pageable));
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