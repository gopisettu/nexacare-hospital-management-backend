package com.nexacare.hospital.mapper.dtotoentity;

import com.nexacare.hospital.dto.request.DoctorProfileDto;
import com.nexacare.hospital.dto.response.AdminRes.DoctorAdminResDto;
import com.nexacare.hospital.model.Doctor;
import com.nexacare.hospital.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DoctorMapper {

    public static  Doctor mapDtotoDoctorEntity(DoctorProfileDto dto, Doctor doctor) {
        doctor.setFirstName(dto.firstName());
        doctor.setLastName(dto.lastName());
        doctor.setGender(dto.gender());
        doctor.setPhone(dto.phone());
        doctor.setEmail(dto.email());
        doctor.setAddress(dto.address());
        doctor.setQualification(dto.qualification());
        doctor.setDepartment(dto.department());
        doctor.setSpecialization(dto.specialization());
        doctor.setTotalExperienceYear(dto.totalExperienceYear());
        doctor.setConsultationFee(dto.consultationFee());

        return doctor;
    }
    public DoctorAdminResDto mapDoctorAdminRes(Doctor doctor) {

        DoctorAdminResDto dto = new DoctorAdminResDto();

        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());

        dto.setUsername(doctor.getUser().getUsername());
        dto.setPhoneNumber(doctor.getPhone());

        dto.setGender(doctor.getGender());
        dto.setDepartment(doctor.getDepartment());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setQualification(doctor.getQualification());

        dto.setExperience(doctor.getTotalExperienceYear());
        dto.setConsultationFee(doctor.getConsultationFee());

        dto.setLicenseNumber(null);
        dto.setProfileImage(doctor.getImageUrl());

        dto.setIsActive(doctor.getUser().isActive());

        dto.setCreatedAt(doctor.getCreatedAt());

        return dto;
    }
}
