package com.nexacare.hospital.mapper.entitytodto;

import com.nexacare.hospital.dto.response.doctorres.DoctorResDto;
import com.nexacare.hospital.model.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorDtoMapper {
    public DoctorResDto mapDoctorEntityToDto(Doctor doctor) {

        return new DoctorResDto(
                doctor.getId(),
                doctor.getUser().getUsername(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getGender(),
                doctor.getPhone(),
                doctor.getEmail(),
                doctor.getAddress(),
                doctor.getQualification(),
                doctor.getDepartment(),
                doctor.getSpecialization(),
                doctor.getTotalExperienceYear(),
                doctor.getConsultationFee()
        );
    }
}
