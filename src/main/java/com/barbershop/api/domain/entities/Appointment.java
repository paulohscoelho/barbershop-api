package com.barbershop.api.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "appointments")
public class Appointment {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @EqualsAndHashCode.Include
   private UUID id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "client_id", nullable = false)
   private User client;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "barber_id", nullable = false)
   private User barber;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "service_id", nullable = false)
   private BarberService service;

   @Column(name = "start_time", nullable = false)
   private Instant startTime;

   @Column(name = "end_time", nullable = false)
   private Instant endTime;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private AppointmentStatus status;

   @Column(name = "created_at", updatable = false, nullable = false)
   private Instant createdAt;

   @PrePersist
   public void prePersist() {
      if (this.createdAt == null) {
         this.createdAt = Instant.now();
      }
      if (this.status == null) {
         this.status = AppointmentStatus.AGENDADO;
      }
   }
}