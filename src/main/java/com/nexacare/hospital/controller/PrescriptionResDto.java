package com.nexacare.hospital.controller;

import com.nexacare.hospital.enums.Frequency;
import com.nexacare.hospital.enums.Route;

public record PrescriptionResDto(
        Long appointmentId,
        String doctorName,
        String medicineName,
        String dosage,
        Frequency frequency,
        Integer durationDays,
        Route route,
        String instructions

) {
}
