package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.AuthenticationDTO;
import com.barbershop.api.domain.dtos.TokenResponseDTO;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.infra.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

   private final AuthenticationManager authenticationManager;
   private final TokenService tokenService;

   public TokenResponseDTO login(AuthenticationDTO dto) {
      var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
      var auth = this.authenticationManager.authenticate(usernamePassword);
      var user = (User) auth.getPrincipal();
      var token = tokenService.generateToken(user);
      return new TokenResponseDTO(token);
   }
}