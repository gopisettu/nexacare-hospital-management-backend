package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.request.*;
import com.nexacare.hospital.dto.response.AppointmentResDto;
import com.nexacare.hospital.enums.AppointmentStatus;
import com.nexacare.hospital.exception.*;
import com.nexacare.hospital.mapper.PrescriptionMapper;
import com.nexacare.hospital.mapper.dtotoentity.AppointmentMapper;
import com.nexacare.hospital.mapper.entitytodto.AppointmentEntityToDto;
import com.nexacare.hospital.model.*;
import com.nexacare.hospital.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentService {
    private  final UserRepository userRepository;
    private  final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
private final AppointmentMapper appointmentMapper;
private final AppointmentRepository appointmentRepository;
private  final AppointmentEntityToDto appointmentEntityToDto;
private final PrescriptionMapper prescriptionMapper;
private final MedicineRepository medicineRepository;
private  final PrescriptionItemRepository prescriptionItemRepository;
    public void bookDoctor(String username, BookAppointmentDto dto) {
        log.info("Patient '{}' is attempting to book an appointment with doctor ID {} on {} at {}",
                username,
                dto.doctorId(),
                dto.appointmentDate(),
                dto.appointmentTime());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient username not found"));

        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(dto.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Check if the doctor already has an appointment for the same date and time
        boolean conflict = appointmentRepository.countConflictingAppointments(
                doctor.getId(),
                dto.appointmentDate(),
                dto.appointmentTime()) > 0;

        if (conflict) {
            log.warn("Booking conflict: Doctor ID {} is already booked on {} at {}",
                    doctor.getId(),
                    dto.appointmentDate(),
                    dto.appointmentTime());

            throw new DoctorAlreadyBookedException("Doctor already booked.");
        }

        Appointment appointment = appointmentMapper.mapDoctorDtoToEntity(dto);

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);

        appointmentRepository.save(appointment);

        log.info("Appointment booked successfully. Patient ID: {}, Doctor ID: {}, Appointment ID: {}",
                patient.getId(),
                doctor.getId(),
                appointment.getId());
    }

    // Doctor views appointments
    public List<AppointmentResDto> showAllAppointmentByDoctor(String username,
                                                              Integer page,
                                                              Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Appointment> appointments =
                appointmentRepository.findByDoctorUserUsername(username, pageable);
        return appointments.stream()
                .map((a) -> appointmentEntityToDto.mapAppointmentEntityToDto(a))
                .toList();
    }

    public List<AppointmentResDto> showAllAppointmentByPatient(String username, Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size);
       List<Appointment> appointments=appointmentRepository.findByPatientUserUsername(username,pageable);
        return appointments.stream()
                .map((a) -> appointmentEntityToDto.mapAppointmentEntityToDto(a))
                .toList();

    }
@Transactional
    public void updateAppointmentStatus(String username, UpdateAppointmentStatusDto updateAppointmentStatusDto) {
//step1 : check valid doctor
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));

//       step 2:get for appointment for patch update
        Appointment appointment=appointmentRepository.findById(updateAppointmentStatusDto.appointmentId())
                .orElseThrow(()->new ResourceNotFoundException("Appointment Id not Found"));
    log.info("Doctor '{}' is updating appointment {} to status {}",
            username,
            updateAppointmentStatusDto.appointmentId(),
            updateAppointmentStatusDto.appointmentStatus());
// step 3. check for the particular appointment belong to the doctor
        if( ! appointment.getDoctor().getId().equals(doctor.getId())){
            log.warn("Unauthorized update attempt. Doctor '{}' tried to update appointment {}",
                    username,
                    appointment.getId());
            throw  new UnauthorizedOperationException("You are not authorized to update this appointment");
        }
//        step 4: update the appointment status as per the dto
        appointment.setAppointmentStatus(updateAppointmentStatusDto.appointmentStatus());
//        step 5: save the appointment
        appointmentRepository.save(appointment);

    log.info("Appointment {} status updated to {}",
            appointment.getId(),
            appointment.getAppointmentStatus());

    }
@Transactional
    public void rescheduleAppointment(String username, RescheduleAppointmentDto rescheduleAppointmentDto) {
//step1 : check valid doctor
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));
//       step 2:get for appointment for patch update
        Appointment appointment=appointmentRepository.findById(rescheduleAppointmentDto.appointmentId())
                .orElseThrow(()->new ResourceNotFoundException("Appointment Id not Found"));
// step 3. check for the particular appointment belong to the doctor
        if( ! appointment.getDoctor().getId().equals(doctor.getId())){
            throw  new UnauthorizedOperationException("You are not authorized to update this appointment");
        }
//        step 4:Check for update status for Already Completed Appointment
        if(appointment.getAppointmentStatus()== AppointmentStatus.COMPLETED){
            log.warn("Reschedule rejected because appointment {} is already completed.",
                    appointment.getId());
            throw new InvalidAppointmentStateException("Unable to update status for Already CompletedAppointment");
        }
//        step5: update the appointment and save it
        appointment.setAppointmentTime(rescheduleAppointmentDto.appointmentTime());
        appointment.setAppointmentDate(rescheduleAppointmentDto.appointmentDate());
        appointment.setAppointmentStatus(rescheduleAppointmentDto.appointmentStatus());
        appointmentRepository.save(appointment);

    log.info("Appointment {} rescheduled to {} {}",
            appointment.getId(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime());
    }

    @Transactional
    public void submitPrescription(String username,
                                   SubmitPrescriptionDto submitPrescriptionDto) {

        // Validate doctor
        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));

        // Validate appointment
        Appointment appointment = appointmentRepository
                .findById(submitPrescriptionDto.appointmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found"));

        // Appointment belongs to logged-in doctor
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            log.warn("Unauthorized prescription attempt by doctor '{}' for appointment {}",
                    username,
                    appointment.getId());
            throw new UnauthorizedOperationException(
                    "You are not authorized to prescribe for this appointment.");
        }

        // Optional: appointment status validation
        if (appointment.getAppointmentStatus() != AppointmentStatus.COMPLETED) {
            log.warn("Prescription rejected because appointment {} is not completed.",
                    appointment.getId());
            throw new IllegalOperationException(
                    "Prescription can only be submitted after the appointment is completed.");
        }

        // Optional: prevent duplicate prescriptions
        if (prescriptionItemRepository.existsByAppointmentId(appointment.getId())) {
            throw new IllegalOperationException(
                    "Prescription has already been submitted for this appointment.");
        }

        for (PrescriptionItemDto dto : submitPrescriptionDto.medicines()) {

            Medicine medicine = medicineRepository.findById(dto.medicineId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Medicine not found: " + dto.medicineId()));

            if (dto.quantity() <= 0) {
                throw new IllegalOperationException("Quantity must be greater than zero.");
            }

            PrescriptionItem item = prescriptionMapper.mapDtoToEntity(dto);

            item.setAppointment(appointment);
            item.setMedicine(medicine);

            prescriptionItemRepository.save(item);
            log.info("Prescription submitted successfully for appointment {}",
                    appointment.getId());
        }
    }
}
