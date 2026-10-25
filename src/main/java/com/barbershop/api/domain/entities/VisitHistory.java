package com.barbershop.api.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "visit_history")
public class VisitHistory {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @EqualsAndHashCode.Include
   private UUID id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "client_id", nullable = false)
   private User client;

   @OneToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "appointment_id", nullable = false, unique = true)
   private Appointment appointment;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "barber_id", nullable = false)
   private User barber;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "service_id", nullable = false)
   private BarberService service;

   @Column(name = "visit_date", nullable = false)
   private Instant visitDate;

   @Column(name = "price_paid", nullable = false)
   private BigDecimal pricePaid;
}
