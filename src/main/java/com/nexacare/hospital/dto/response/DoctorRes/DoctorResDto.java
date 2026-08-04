package com.nexacare.hospital.dto.response.DoctorRes;

import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.Qualification;
import com.nexacare.hospital.enums.Specialization;

public record DoctorResDto(

        Long id,

        String username,

        String firstName,

        String lastName,

        Gender gender,

        String phone,

        String email,

        String address,

        Qualification qualification,

        Department department,

        Specialization specialization,

        Integer totalExperienceYear,

        Double consultationFee

) {
}