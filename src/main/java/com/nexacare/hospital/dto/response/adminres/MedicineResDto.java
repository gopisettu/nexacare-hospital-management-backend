package com.nexacare.hospital.dto.response.adminres;

public record MedicineResDto(
        Long id,
        String name,
        String dosage,
        String manufacturer
) {
}
