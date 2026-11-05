package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.AppointmentRequestDTO;
import com.barbershop.api.domain.dtos.AppointmentResponseDTO;
import com.barbershop.api.domain.dtos.DashboardStatsDTO;
import com.barbershop.api.domain.entities.*;
import com.barbershop.api.domain.exceptions.RegraNegocioException;
import com.barbershop.api.domain.mappers.AppointmentMapper;
import com.barbershop.api.domain.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

   private final UserRepository userRepository;
   private final BarberServiceRepository barberServiceRepository;
   private final AppointmentRepository appointmentRepository;
   private final ScheduleBlockRepository scheduleBlockRepository;
   private final AppointmentMapper appointmentMapper;
   private final WorkingHourRepository workingHourRepository;

   @Transactional(isolation = Isolation.SERIALIZABLE)
   public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto, User client) {
      User barber = userRepository.findById(dto.barberId())
          .orElseThrow(() -> new RegraNegocioException("Barbeiro não encontrado."));

      if (barber.getRole() != UserRole.BARBER) {
         throw new RegraNegocioException("O usuário selecionado não possui a role de BARBEIRO.");
      }

      List<BarberService> services = barberServiceRepository.findAllById(dto.serviceIds());

      if (services.isEmpty()) {
         throw new RegraNegocioException("Nenhum serviço válido selecionado.");
      }

      int totalDurationMinutes = services.stream().mapToInt(BarberService::getDurationMinutes).sum();
      Instant startTime = dto.startTime();
      Instant endTime = startTime.plus(totalDurationMinutes, ChronoUnit.MINUTES);

      boolean barberIsBlocked = scheduleBlockRepository.hasBlockageInInterval(barber.getId(), startTime, endTime);
      if (barberIsBlocked) {
         throw new RegraNegocioException("O barbeiro está indisponível neste horário (almoço/folga).");
      }

      boolean isDoubleBooked = appointmentRepository.hasConflictingAppointment(barber.getId(), startTime, endTime, AppointmentStatus.CANCELADO);
      if (isDoubleBooked) {
         throw new RegraNegocioException("Este horário já está ocupado por outro agendamento.");
      }

      Appointment appointment = appointmentMapper.toEntity(dto, client, barber, services, endTime);
      Appointment savedAppointment = appointmentRepository.save(appointment);

      return appointmentMapper.toResponse(savedAppointment);
   }

   @Transactional(readOnly = true)
   public Page<AppointmentResponseDTO> listAppointmentsByClient(UUID clientId, Pageable pageable) {
      Page<Appointment> appointments = appointmentRepository.findByClientId(clientId, pageable);
      return appointments.map(appointmentMapper::toResponse);
   }

   @Transactional
   public AppointmentResponseDTO cancelAppointment(UUID appointmentId) {
      Appointment appointment = appointmentRepository.findById(appointmentId)
          .orElseThrow(() -> new RegraNegocioException("Agendamento não encontrado."));

      if (appointment.getStatus() == AppointmentStatus.CANCELADO || appointment.getStatus() == AppointmentStatus.FINALIZADO) {
         throw new RegraNegocioException("Este agendamento não pode mais ser cancelado.");
      }

      appointment.setStatus(AppointmentStatus.CANCELADO);
      return appointmentMapper.toResponse(appointmentRepository.save(appointment));
   }

   @Transactional
   public AppointmentResponseDTO completeAppointment(UUID appointmentId) {
      Appointment appointment = appointmentRepository.findById(appointmentId)
          .orElseThrow(() -> new RegraNegocioException("Agendamento não encontrado."));

      if (appointment.getStatus() == AppointmentStatus.CANCELADO || appointment.getStatus() == AppointmentStatus.FINALIZADO) {
         throw new RegraNegocioException("Este agendamento não pode ser concluído.");
      }

      appointment.setStatus(AppointmentStatus.FINALIZADO);

      User client = appointment.getClient();
      client.setLoyaltyCount(client.getLoyaltyCount() + 1);
      userRepository.save(client);

      return appointmentMapper.toResponse(appointmentRepository.save(appointment));
   }


   @Transactional(readOnly = true)
   public DashboardStatsDTO getDashboardStats(UUID barberId) {
      Long total = appointmentRepository.countByBarberId(barberId);
      Long agendados = appointmentRepository.countByBarberIdAndStatus(barberId, AppointmentStatus.AGENDADO);
      Long finalizados = appointmentRepository.countByBarberIdAndStatus(barberId, AppointmentStatus.FINALIZADO);
      Long cancelados = appointmentRepository.countByBarberIdAndStatus(barberId, AppointmentStatus.CANCELADO);

      return new DashboardStatsDTO(total, agendados, finalizados, cancelados);
   }

   @Transactional(readOnly = true)
   public List<String> getAvailableSlots(UUID barberId, LocalDate date, List<UUID> serviceIds) {
      List<BarberService> services = barberServiceRepository.findAllById(serviceIds);
      if (services.isEmpty()) {
         throw new RegraNegocioException("Nenhum serviço válido selecionado.");
      }
      int totalDuration = services.stream().mapToInt(BarberService::getDurationMinutes).sum();

      int dayOfWeek = date.getDayOfWeek().getValue();
      List<WorkingHour> workingHours = workingHourRepository.findByBarberIdAndDayOfWeek(barberId, dayOfWeek);

      if (workingHours.isEmpty()) {
         return List.of();
      }

      Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();
      Instant endOfDay = date.atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant();

      List<Appointment> existingAppointments = appointmentRepository.findByBarberIdAndStartTimeBetween(barberId, startOfDay, endOfDay);
      List<ScheduleBlock> existingBlocks = scheduleBlockRepository.findByBarberIdAndStartTimeBetween(barberId, startOfDay, endOfDay);

      List<String> availableSlots = new ArrayList<>();

      for (WorkingHour wh : workingHours) {
         LocalTime current = wh.getStartTime();
         LocalTime end = wh.getEndTime();

         while (!current.plusMinutes(totalDuration).isAfter(end)) {
            Instant slotStart = date.atTime(current).atZone(ZoneOffset.UTC).toInstant();
            Instant slotEnd = slotStart.plus(totalDuration, ChronoUnit.MINUTES);

            boolean hasAppointmentConflict = existingAppointments.stream()
                .anyMatch(app -> app.getStartTime().isBefore(slotEnd) && app.getEndTime().isAfter(slotStart));

            boolean hasBlockConflict = existingBlocks.stream()
                .anyMatch(block -> block.getStartTime().isBefore(slotEnd) && block.getEndTime().isAfter(slotStart));

            if (!hasAppointmentConflict && !hasBlockConflict) {
               availableSlots.add(current.toString());
            }

            current = current.plusMinutes(30);
         }
      }

      return availableSlots;
   }

   @Transactional(readOnly = true)
   public Page<AppointmentResponseDTO> listAppointmentsByBarber(UUID barberId, AppointmentStatus status, String clientName, Pageable pageable) {
      Page<Appointment> appointments = appointmentRepository.findByBarberAndFilters(barberId, status, clientName, pageable);
      return appointments.map(appointmentMapper::toResponse);
   }
}