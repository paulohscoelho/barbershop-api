package com.barbershop.api.domain.repositories;

import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

   Optional<User> findByEmail(String email);
   List<User> findByRole(UserRole role);
   boolean existsByEmail(String email);
}
