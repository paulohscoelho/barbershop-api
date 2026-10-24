package com.barbershop.api.domain.controllers;

import com.barbershop.api.domain.dtos.UserRequestDTO;
import com.barbershop.api.domain.dtos.UserResponseDTO;
import com.barbershop.api.domain.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

   private final UserService userService;

   @PostMapping
   public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO dto) {
      UserResponseDTO response = userService.createUser(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }
}