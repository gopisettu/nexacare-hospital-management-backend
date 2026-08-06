package com.nexacare.hospital.dto.request.DoctorReq;

import com.nexacare.hospital.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentStatusDto(
        @NotNull(message = "Appointment ID is mandatory")
        Long appointmentId,
        @NotNull(message = "Appointment status is mandatory")
        AppointmentStatus appointmentStatus
) {
}
