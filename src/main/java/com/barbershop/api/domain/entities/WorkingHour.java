package com.barbershop.api.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "working_hours")
public class WorkingHour {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @EqualsAndHashCode.Include
   private UUID id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "barber_id", nullable = false)
   private User barber;

   @Column(name = "day_of_week", nullable = false)
   private Integer dayOfWeek;

   @Column(name = "start_time", nullable = false)
   private LocalTime startTime;

   @Column(name = "end_time", nullable = false)
   private LocalTime endTime;

   @Column(name = "lunch_start_time")
   private LocalTime lunchStartTime;

   @Column(name = "lunch_end_time")
   private LocalTime lunchEndTime;

   @Column(nullable = false)
   private Boolean active = true;
}