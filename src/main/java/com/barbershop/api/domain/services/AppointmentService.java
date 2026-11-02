package com.barbershop.api.domain.services;

import com.barbershop.api.domain.dtos.AppointmentRequestDTO;
import com.barbershop.api.domain.dtos.AppointmentResponseDTO;
import com.barbershop.api.domain.entities.*;
import com.barbershop.api.domain.exceptions.RegraNegocioException;
import com.barbershop.api.domain.mappers.AppointmentMapper;
import com.barbershop.api.domain.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

   private final UserRepository userRepository;
   private final BarberServiceRepository barberServiceRepository;
   private final AppointmentRepository appointmentRepository;
   private final ScheduleBlockRepository scheduleBlockRepository;
   private final AppointmentMapper appointmentMapper;

   @Transactional
   public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {
      User client = userRepository.findById(dto.clientId())
          .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado."));

      User barber = userRepository.findById(dto.barberId())
          .orElseThrow(() -> new RegraNegocioException("Barbeiro não encontrado."));

      if (barber.getRole() != UserRole.BARBER) {
         throw new RegraNegocioException("O usuário selecionado não possui a role de BARBEIRO.");
      }

      BarberService service = barberServiceRepository.findById(dto.serviceId())
          .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado."));

      if (!service.getActive()) {
         throw new RegraNegocioException("Este serviço está inativo.");
      }

      Instant startTime = dto.startTime();
      Instant endTime = startTime.plus(service.getDurationMinutes(), ChronoUnit.MINUTES);

      boolean barberIsBlocked = scheduleBlockRepository.hasBlockageInInterval(
          barber.getId(), startTime, endTime
      );
      if (barberIsBlocked) {
         throw new RegraNegocioException("O barbeiro está indisponível neste horário (almoço/folga).");
      }

      boolean isDoubleBooked = appointmentRepository.hasConflictingAppointment(
          barber.getId(), startTime, endTime, AppointmentStatus.CANCELADO
      );
      if (isDoubleBooked) {
         throw new RegraNegocioException("Este horário já está ocupado por outro agendamento.");
      }

      Appointment appointment = appointmentMapper.toEntity(dto, client, barber, service, endTime);
      Appointment savedAppointment = appointmentRepository.save(appointment);

      return appointmentMapper.toResponse(savedAppointment);
   }

   @Transactional(readOnly = true)
   public List<AppointmentResponseDTO> listAppointmentsByClient(UUID clientId) {
      return appointmentRepository.findByClientId(clientId)
          .stream()
          .map(appointmentMapper::toResponse)
          .toList();
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
}