package com.nexacare.hospital.dto.request.DoctorReq;

import com.nexacare.hospital.enums.Reason;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookAppointmentDto(
        @NotNull(message = "Doctor is mandatory")
        Long doctorId,
        @NotNull(message = "Appointment date is mandatory")
        @FutureOrPresent(message = "Appointment date cannot be in the past")
        LocalDate appointmentDate,
        @NotNull(message = "Appointment time is mandatory")
        LocalTime appointmentTime,
        @NotNull(message = "Reason for appointment is mandatory")
        Reason reason,
        String notes
        //appointment status update in service layer

) {
}