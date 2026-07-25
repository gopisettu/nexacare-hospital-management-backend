package com.nexacare.hospital.mapper.entitytodto;

import com.nexacare.hospital.dto.response.PatientResDto;
import com.nexacare.hospital.model.Patient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PatientEntityMapper {
    public PatientResDto mapPatientEntityToDto(Patient patinet){

       return  new PatientResDto
                (patinet.getId(),
                        patinet.getUser().getUsername(),
                        patinet.getFirstName(),
                        patinet.getLastName(),
                        patinet.getGender(),
                        patinet.getDob(),
                        patinet.getAadharNumber(),
                        patinet.getBloodGroup(),
                        patinet.getPhone(),
                        patinet.getEmail(),
                        patinet.getAddress(),
                        patinet.getAllergies(),
                        patinet.getChronicDisease()
                );



    }
}
