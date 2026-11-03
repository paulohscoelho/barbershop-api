package com.barbershop.api.domain.controllers;

import com.barbershop.api.domain.dtos.ChangePasswordDTO;
import com.barbershop.api.domain.dtos.UserRequestDTO;
import com.barbershop.api.domain.dtos.UserResponseDTO;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

   @GetMapping
   public ResponseEntity<List<UserResponseDTO>> listAll() {
      return ResponseEntity.ok(userService.listAllUsers());
   }

   @GetMapping("/{id}")
   public ResponseEntity<UserResponseDTO> getById(@PathVariable UUID id) {
      return ResponseEntity.ok(userService.getUserById(id));
   }

   @PatchMapping("/{id}/promote-barber")
   public ResponseEntity<UserResponseDTO> promoteToBarber(@PathVariable UUID id) {
      return ResponseEntity.ok(userService.promoteToBarber(id));
   }

   @PatchMapping("/change-password")
   public ResponseEntity<Void> changePassword(
       @RequestBody @Valid ChangePasswordDTO dto,
       @AuthenticationPrincipal User loggedUser) {

      userService.changePassword(loggedUser, dto);
      return ResponseEntity.noContent().build();
   }
}