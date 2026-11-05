package com.barbershop.api.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "special_schedules")
public class SpecialSchedule {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @EqualsAndHashCode.Include
   private UUID id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "barber_id", nullable = false)
   private User barber;

   @Column(name = "exception_date", nullable = false)
   private LocalDate exceptionDate;

   @Column(name = "is_closed", nullable = false)
   private Boolean isClosed = false;

   @Column(name = "start_time")
   private LocalTime startTime;

   @Column(name = "end_time")
   private LocalTime endTime;

   private String description;
}