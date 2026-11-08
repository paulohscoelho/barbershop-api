package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.ChangePasswordDTO;
import com.barbershop.api.domain.dtos.UpdateProfileDTO;
import com.barbershop.api.domain.dtos.UserRequestDTO;
import com.barbershop.api.domain.dtos.UserResponseDTO;
import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.entities.UserRole;
import com.barbershop.api.domain.exceptions.RegraNegocioException;
import com.barbershop.api.domain.mappers.UserMapper;
import com.barbershop.api.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

   @Transactional(readOnly = true)
   public List<UserResponseDTO> listAllUsers() {
      return userRepository.findAll()
          .stream()
          .map(userMapper::toResponse)
          .toList();
   }

   @Transactional(readOnly = true)
   public List<UserResponseDTO> listAllClients() {
      return userRepository.findByRole(UserRole.CLIENT)
          .stream()
          .map(userMapper::toResponse)
          .toList();
   }

   @Transactional(readOnly = true)
   public UserResponseDTO getUserById(UUID id) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));
      return userMapper.toResponse(user);
   }

   @Transactional
   public UserResponseDTO promoteToBarber(UUID id) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));

      if (user.getRole() == UserRole.BARBER) {
         throw new RegraNegocioException("Este usuário já é um barbeiro.");
      }

      if (user.getRole() == UserRole.ADMIN) {
         throw new RegraNegocioException("Não é possível alterar a role de um Administrador.");
      }

      user.setRole(UserRole.BARBER);
      return userMapper.toResponse(userRepository.save(user));
   }

   @Transactional
   public void changePassword(User loggedUser, ChangePasswordDTO dto) {
      if (!passwordEncoder.matches(dto.oldPassword(), loggedUser.getPasswordHash())) {
         throw new RegraNegocioException("A senha antiga está incorreta.");
      }

      if (dto.oldPassword().equals(dto.newPassword())) {
         throw new RegraNegocioException("A nova senha não pode ser igual à senha antiga.");
      }

      loggedUser.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
      userRepository.save(loggedUser);
   }

   @Transactional(readOnly = true)
   public List<UserResponseDTO> listBarbers() {
      return userRepository.findByRole(UserRole.BARBER)
          .stream()
          .map(userMapper::toResponse)
          .toList();
   }

   @Transactional(readOnly = true)
   public UserResponseDTO getMyProfile(User loggedUser) {
      return userMapper.toResponse(loggedUser);
   }

   @Transactional
   public UserResponseDTO updateLoyaltyGoal(User loggedUser, Integer newGoal) {
      if (loggedUser.getRole() != UserRole.BARBER && loggedUser.getRole() != UserRole.ADMIN) {
         throw new RegraNegocioException("Apenas barbeiros podem alterar a meta de fidelidade.");
      }
      if (newGoal < 1) {
         throw new RegraNegocioException("A meta deve ser de pelo menos 1 atendimento.");
      }
      loggedUser.setLoyaltyGoal(newGoal);
      return userMapper.toResponse(userRepository.save(loggedUser));
   }

   @Transactional
   public UserResponseDTO updateProfile(User loggedUser, UpdateProfileDTO dto) {
      if (!loggedUser.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
         throw new RegraNegocioException("Este e-mail já está em uso por outro usuário.");
      }

      loggedUser.setName(dto.name());
      loggedUser.setEmail(dto.email());
      loggedUser.setPhone(dto.phone());

      return userMapper.toResponse(userRepository.save(loggedUser));
   }
}