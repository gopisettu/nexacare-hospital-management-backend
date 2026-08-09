package com.nexacare.hospital.dto.request.DoctorReq;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SubmitPrescriptionDto(

        @NotNull(message = "Appointment ID is mandatory")
        Long appointmentId,

        @NotNull(message = "Prescription is mandatory")
        @Size(min = 1, message = "At least one medicine is required")
        List<PrescriptionItemDto> medicines

) {
}