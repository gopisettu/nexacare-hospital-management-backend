package com.nexacare.hospital.dto.request.doctorreq;

public record DoctorFilterRequest(
        String search,
        String gender,
        String department,
        String specialization,
        String qualification,
        String feeSort,
        String experienceSort
) {
}