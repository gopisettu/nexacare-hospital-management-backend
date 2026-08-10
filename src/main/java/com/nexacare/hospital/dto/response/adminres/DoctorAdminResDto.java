package com.nexacare.hospital.dto.response.adminres;


import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.Qualification;
import com.nexacare.hospital.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAdminResDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;      // Email/Login

    private String phoneNumber;

    private Gender gender;

    private Department department;

    private Specialization specialization;

    private Qualification qualification;

    private Integer experience;

    private Double consultationFee;

    private String licenseNumber;

    private String profileImage;

    private Boolean isActive;

    private Instant createdAt;
}