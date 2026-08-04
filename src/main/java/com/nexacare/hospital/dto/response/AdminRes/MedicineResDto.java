package com.nexacare.hospital.dto.response.AdminRes;

public record MedicineResDto(
        Long id,
        String name,
        String dosage,
        String manufacturer
) {
}
