package com.nexacare.hospital.dto.request;
import com.nexacare.hospital.enums.MedicineForm;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AddMedicineDto(

        @NotBlank
        String name,

        @NotBlank
        String dosage,

        @NotBlank
        String manufacturer,

        @NotBlank
        String category,

        @NotNull
        MedicineForm medicineForm,

        @NotNull

        Double unitPrice,

        @NotBlank
        String batchNo,

        @NotNull
        Integer quantityReceived,

        @NotNull

        LocalDate expiryDate
) {
}