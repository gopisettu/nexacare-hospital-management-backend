package com.nexacare.hospital.dto.request.AdminReq;


import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.Qualification;
import com.nexacare.hospital.enums.Specialization;
import jakarta.validation.constraints.*;

public record DoctorRegisterByAdminDto(

        @NotBlank(message = "Username is mandatory")
        String username,

        @NotBlank(message = "Password is mandatory")
        String password,

        @NotBlank(message = "First name is mandatory")
        @Size(max = 50)
        String firstName,

        @Size(max = 50)
        String lastName,

        @NotNull
        Gender gender,

        @NotBlank
        @Pattern(regexp = "\\d{10}")
        String phone,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String address,

        @NotNull
        Qualification qualification,

        @NotNull
        Department department,

        @NotNull
        Specialization specialization,

        @NotNull
        @Min(0)
        @Max(60)
        Integer totalExperienceYear,

        Double consultationFee

) {
}