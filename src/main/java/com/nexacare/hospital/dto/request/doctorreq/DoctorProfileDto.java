package com.nexacare.hospital.dto.request.doctorreq;

import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.Qualification;
import com.nexacare.hospital.enums.Specialization;
import jakarta.validation.constraints.*;

public record DoctorProfileDto(

        @NotBlank(message = "First name is mandatory")
        @Size(max = 50, message = "First name cannot exceed 50 characters")
        String firstName,

        @Size(max = 50, message = "Last name cannot exceed 50 characters")
        String lastName,

        @NotNull(message = "Gender is mandatory")
        Gender gender,

        @NotBlank(message = "Phone number is mandatory")
        @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
        String phone,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Address is mandatory")
        @Size(max = 255, message = "Address cannot exceed 255 characters")
        String address,

        @NotNull(message = "Qualification is mandatory")
        Qualification qualification,

        @NotNull(message = "Department is mandatory")
        Department department,

        @NotNull(message = "Specialization is mandatory")
        Specialization specialization,

        @NotNull(message = "Total experience is mandatory")
        @Min(value = 0, message = "Experience cannot be negative")
        @Max(value = 60, message = "Experience cannot exceed 60 years")
        Integer totalExperienceYear,

        @NotNull(message = "Consultation fee is mandatory")
        @Positive(message = "Consultation fee must be greater than zero")
        Double consultationFee

) {
}