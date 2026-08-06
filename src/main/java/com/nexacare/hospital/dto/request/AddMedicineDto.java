package com.nexacare.hospital.dto.request;
import com.nexacare.hospital.enums.MedicineForm;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record AddMedicineDto(

        @NotBlank(message = "Medicine name is mandatory")
        String name,

        @NotBlank(message = "Dosage is mandatory")
        String dosage,

        @NotBlank(message = "Manufacturer is mandatory")
        String manufacturer,

        @NotBlank(message = "Category is mandatory")
        String category,

        @NotNull(message = "Medicine form is mandatory")
        MedicineForm medicineForm,

        @NotNull(message = "Unit price is mandatory")
        @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")

        Double unitPrice,

        @NotBlank(message = "Batch number is mandatory")
        String batchNo,

        @NotNull(message = "Quantity received is mandatory")
        @Min(value = 1, message = "Quantity received must be at least 1")
        Integer quantityReceived,

        @NotNull(message = "Expiry date is mandatory")
        @Future(message = "Expiry date must be a future date")

        LocalDate expiryDate
) {
}