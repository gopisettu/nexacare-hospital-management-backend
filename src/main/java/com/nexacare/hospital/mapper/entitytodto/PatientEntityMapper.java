package com.nexacare.hospital.mapper.entitytodto;

import com.nexacare.hospital.dto.response.adminres.PatientAdminResDto;
import com.nexacare.hospital.dto.response.patientres.PatientResDto;
import com.nexacare.hospital.model.Appointment;
import com.nexacare.hospital.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientEntityMapper {
    public PatientResDto mapPatientEntityToDto(Patient patinet) {

        return new PatientResDto
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

    public PatientAdminResDto mapPatientAdminRes(Patient patient, Appointment appointment) {

        return new PatientAdminResDto(
                patient.getId(),
                patient.getUser().getUsername(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getGender(),
                patient.getDob(),
                patient.getAadharNumber(),
                patient.getBloodGroup(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getAllergies(),
                patient.getChronicDisease(),
                appointment != null ? appointment.getPaymentStatus() : null,
                appointment != null ? appointment.getAppointmentDate() : null,
                appointment != null ? appointment.getAppointmentTime() : null,
                appointment !=null?appointment.getAppointmentStatus():null,
                patient.getImageUrl()
        );
    }
}
