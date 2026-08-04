package com.nexacare.hospital.dto.response.DoctorRes;

import com.nexacare.hospital.enums.AppointmentStatus;
import com.nexacare.hospital.enums.Reason;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResDto(
        Long patientId,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        Reason reason,
        String notes,
        AppointmentStatus appointmentStatus,
        Long doctorId,
        String doctorName,
        String patientName,
        Long appointmentId
) {
}
