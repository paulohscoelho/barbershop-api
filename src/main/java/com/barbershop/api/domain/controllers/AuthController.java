package com.barbershop.api.domain.controllers;

import com.barbershop.api.domain.dtos.AuthenticationDTO;
import com.barbershop.api.domain.dtos.TokenResponseDTO;
import com.barbershop.api.domain.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

   private final AuthService authService;

   @PostMapping("/login")
   public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid AuthenticationDTO dto) {
      return ResponseEntity.ok(authService.login(dto));
   }
}