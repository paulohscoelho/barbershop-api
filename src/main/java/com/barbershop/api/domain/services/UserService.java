package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.UserRequestDTO;
import com.barbershop.api.domain.dtos.UserResponseDTO;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.exceptions.RegraNegocioException;
import com.barbershop.api.domain.mappers.UserMapper;
import com.barbershop.api.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;
   private final UserMapper userMapper;
   private final PasswordEncoder passwordEncoder;

   @Transactional
   public UserResponseDTO createUser(UserRequestDTO dto) {

      if (userRepository.existsByEmail(dto.email())) {
         throw new RegraNegocioException("E-mail já cadastrado no sistema.");
      }

      User user = userMapper.toEntity(dto);
      user.setPasswordHash(passwordEncoder.encode(dto.password()));
      User savedUser = userRepository.save(user);

      return userMapper.toResponse(savedUser);
   }
}