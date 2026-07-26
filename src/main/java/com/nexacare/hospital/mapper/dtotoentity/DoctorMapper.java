package com.nexacare.hospital.mapper.dtotoentity;

import com.nexacare.hospital.dto.request.DoctorProfileDto;
import com.nexacare.hospital.model.Doctor;
import com.nexacare.hospital.model.User;
import org.springframework.stereotype.Component;

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
}
