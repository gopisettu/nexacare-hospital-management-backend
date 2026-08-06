package com.nexacare.hospital.dto.request.DoctorReq;

import com.nexacare.hospital.enums.Frequency;
import com.nexacare.hospital.enums.Route;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PrescriptionItemDto(

        @NotNull(message = "Medicine is mandatory")
        Long medicineId,
        @NotNull(message = "Quantity is mandatory")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,
        @NotBlank(message = "Dosage is mandatory")
        String dosage,
        @NotNull(message = "Frequency is mandatory")
        Frequency frequency,

        @NotNull(message = "Duration is mandatory")
        Integer durationDays,

        Route route,

        String instruction

) {}