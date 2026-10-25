package com.barbershop.api.domain.entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "schedule_blocks")
public class ScheduleBlock {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @EqualsAndHashCode.Include
   private UUID id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "barber_id", nullable = false)
   private User barber;

   @Column(name = "start_time", nullable = false)
   private Instant startTime;

   @Column(name = "end_time", nullable = false)
   private Instant endTime;

   @Column(nullable = false)
   private String reason;
}