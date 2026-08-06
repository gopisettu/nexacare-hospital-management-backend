package com.nexacare.hospital.dto.request.DoctorReq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubmitPrescriptionDto(

        @NotNull(message = "Appointment ID is mandatory")
        Long appointmentId,
        @NotBlank(message = "Prescription is mandatory")
        List<PrescriptionItemDto> medicines

) {
}