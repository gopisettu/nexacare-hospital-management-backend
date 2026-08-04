package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.response.DoctorRes.PrescriptionResDto;
import com.nexacare.hospital.exception.ResourceNotFoundException;
import com.nexacare.hospital.mapper.entitytodto.PrescriptionItemToDtoMapper;
import com.nexacare.hospital.model.PrescriptionItem;
import com.nexacare.hospital.repositories.AppointmentRepository;
import com.nexacare.hospital.repositories.PatientRepository;
import com.nexacare.hospital.repositories.PrescriptionItemRepository;
import com.nexacare.hospital.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PrescriptionService {
    private  final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private  final PrescriptionItemRepository prescriptionItemRepository;
    private  final AppointmentRepository appointmentRepository;
    private final PrescriptionItemToDtoMapper prescriptionItemToDtoMapper;
    public List<PrescriptionResDto> viewPrescription(String username, Long appointmentId) {
       userRepository.findByUsername(username)
                .orElseThrow(()->{
                    log.warn("User '{}' not found during authentication.", username);
                   return new ResourceNotFoundException("Patient Not Found");

                        }
                );

        appointmentRepository.findById(appointmentId)
                .orElseThrow(()->
                {log.warn("Appointment Not found for the id",appointmentId);
                    return  new ResourceNotFoundException("Appointment Not Found");
                });


            List<PrescriptionItem> prescriptionItem=prescriptionItemRepository.findPrescriptionByAppointmentId(appointmentId);
        log.warn("Prescription Successfully Reterived  for the Patient",username);
        return
                prescriptionItem.stream()
                                .map((p)->prescriptionItemToDtoMapper.mapPrescriptionEntityToDto(p))
                                        .toList();

    }
}
