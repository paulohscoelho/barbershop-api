package com.barbershop.api.domain.controllers;

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

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

   private final AppointmentService appointmentService;

   @PostMapping
   public ResponseEntity<AppointmentResponseDTO> createAppointment(
       @RequestBody @Valid AppointmentRequestDTO dto
   ) {
      AppointmentResponseDTO response = appointmentService.createAppointment(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }
}