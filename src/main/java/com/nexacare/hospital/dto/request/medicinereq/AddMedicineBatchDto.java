package com.nexacare.hospital.dto.request.medicinereq;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AddMedicineBatchDto(

        @NotNull(message = "Medicine ID is required")
        Long medicineId,

        @NotBlank(message = "Batch number is required")
        String batchNo,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be greater than 0")
        Integer quantityReceived,

        @NotNull(message = "Expiry date is required")

        LocalDate expiryDate
) {
}