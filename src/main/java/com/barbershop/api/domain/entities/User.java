package com.barbershop.api.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User implements UserDetails {

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

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      if (this.role == UserRole.ADMIN) {
         return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
             new SimpleGrantedAuthority("ROLE_BARBER"),
             new SimpleGrantedAuthority("ROLE_CLIENT")
         );
      }
      if (this.role == UserRole.BARBER) {
         return List.of(new SimpleGrantedAuthority("ROLE_BARBER"), new SimpleGrantedAuthority("ROLE_CLIENT"));
      }
      return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
   }

   @Override
   public String getPassword() {
      return this.passwordHash;
   }

   @Override
   public String getUsername() {
      return this.email;
   }

   @Override
   public boolean isAccountNonExpired() { return true; }

   @Override
   public boolean isAccountNonLocked() { return true; }

   @Override
   public boolean isCredentialsNonExpired() { return true; }

   @Override
   public boolean isEnabled() { return true; }

   @Column(name = "loyalty_goal", nullable = false)
   private Integer loyaltyGoal = 10;
}