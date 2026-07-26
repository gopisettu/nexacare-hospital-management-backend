package com.nexacare.hospital.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubmitPrescriptionDto(

        @NotNull
        Long appointmentId,
        List<PrescriptionItemDto> medicines

) {
}