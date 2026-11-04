package com.barbershop.api.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "barber_service")
public class BarberService {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @EqualsAndHashCode.Include
   private UUID id;

   @Column(nullable = false)
   private String name;

   private String description;

   @Column(name = "duration_minutes", nullable = false)
   private Integer durationMinutes;

   @Column(nullable = false)
   private BigDecimal price;

   @Column(nullable = false)
   private Boolean active = true;

   @ManyToMany(mappedBy = "services")
   private List<Appointment> appointments = new ArrayList<>();
}