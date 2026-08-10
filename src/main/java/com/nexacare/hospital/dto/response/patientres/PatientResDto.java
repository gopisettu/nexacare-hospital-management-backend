package com.nexacare.hospital.dto.response.patientres;

import com.nexacare.hospital.enums.BloodGroup;
import com.nexacare.hospital.enums.Gender;

import java.time.LocalDate;

public record PatientResDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        Gender gender,
        LocalDate dob,
        String aadharNumber,
        BloodGroup bloodGroup,
        String phone,
        String email,
        String address,
        String allergies,
        String chronicDisease
) {
}
