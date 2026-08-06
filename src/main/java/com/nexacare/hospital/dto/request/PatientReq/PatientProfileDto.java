package com.nexacare.hospital.dto.request.PatientReq;

import com.nexacare.hospital.enums.BloodGroup;
import com.nexacare.hospital.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record PatientProfileDto(

        @NotBlank(message = "First name is mandatory")
        String firstName,
        String lastName,
        Gender gender,
        @Past(message = "Date of birth must be in the past")
        LocalDate dob,
        @NotBlank(message = "Aadhaar number is mandatory")
        @Pattern(regexp = "\\d{12}", message = "Aadhaar must be 12 digits")
        String aadharNumber,
        BloodGroup bloodGroup,
        @NotBlank(message = "Phone is mandatory")
        String phone,
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email must be valid")
        String email,
        @NotBlank(message = "Address is mandatory")
        String address,

        String allergies,
        String chronicDisease
) {
}
