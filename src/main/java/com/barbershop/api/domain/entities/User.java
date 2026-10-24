package com.barbershop.api.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @EqualsAndHashCode.Include
   private UUID id;

   private String name;

   private String email;

   @Column(name = "password_hash")
   private String passwordHash;

   private String phone;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private UserRole role;

   @Column(name = "loyalty_count", nullable = false)
   private Integer loyaltyCount = 0;

   @Column(name = "created_at", updatable = false, nullable = false)
   private Instant createdAt;

   @PrePersist
   public void prePersist() {
      if (this.createdAt == null) {
         this.createdAt = Instant.now();
      }
   }
}