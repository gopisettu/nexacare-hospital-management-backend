package com.nexacare.hospital.mapper.dtotoentity;

import com.nexacare.hospital.dto.request.PatientProfileDto;
import com.nexacare.hospital.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientDtoMapper {

 public static Patient mapDtoToPatient(PatientProfileDto patientProfileDto,Patient patient){

     patient.setFirstName(patientProfileDto.firstName());
     patient.setLastName(patientProfileDto.lastName());
     patient.setGender(patientProfileDto.gender());
     patient.setDob(patientProfileDto.dob());
     patient.setAadharNumber(patient.getAadharNumber());
     patient.setBloodGroup(patientProfileDto.bloodGroup());
     patient.setPhone(patientProfileDto.phone());
     patient.setEmail(patientProfileDto.email());
     patient.setAddress(patientProfileDto.address());
     patient.setAllergies(patient.getAllergies());
     patient.setChronicDisease(patient.getChronicDisease());
     return patient;
 }

}
