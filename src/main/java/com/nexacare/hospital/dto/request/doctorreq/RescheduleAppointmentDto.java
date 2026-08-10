package com.nexacare.hospital.dto.request.doctorreq;

import com.nexacare.hospital.enums.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleAppointmentDto(


        @NotNull(message = "Appointment ID is mandatory")
        Long appointmentId,
        @Future(message = "Appointment date must be in the future")
        LocalDate appointmentDate,
        @NotNull(message = "Appointment time is mandatory")
        LocalTime appointmentTime,
        @NotNull(message = "Appointment time is mandatory")
        AppointmentStatus appointmentStatus
) {
}

