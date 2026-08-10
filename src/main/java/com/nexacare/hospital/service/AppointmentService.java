package com.nexacare.hospital.service;

import com.nexacare.hospital.controller.PatientAppointmentResponseDto;
import com.nexacare.hospital.dto.request.DoctorReq.*;
import com.nexacare.hospital.dto.request.PatientReq.PayBillDto;
import com.nexacare.hospital.dto.response.DoctorRes.AppointmentResDto;
import com.nexacare.hospital.enums.AppointmentPeriod;
import com.nexacare.hospital.enums.AppointmentStatus;
import com.nexacare.hospital.enums.PaymentStatus;
import com.nexacare.hospital.exception.*;
import com.nexacare.hospital.mapper.PrescriptionMapper;
import com.nexacare.hospital.mapper.dtotoentity.AppointmentMapper;
import com.nexacare.hospital.mapper.entitytodto.AppointmentEntityToDto;
import com.nexacare.hospital.model.*;
import com.nexacare.hospital.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
private final MedicineBatchRepository medicineBatchRepository;
private  final BillingService billingService;
private final InventoryService inventoryService;
    private static final String DOCTOR_NOT_FOUND = "Doctor not found";
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
                .orElseThrow(() -> new ResourceNotFoundException(DOCTOR_NOT_FOUND));

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
    public Page<AppointmentResDto> showAllAppointmentByDoctor(
            String username,
            AppointmentPeriod period,
            AppointmentStatus status,
            Integer page,
            Integer size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.ASC,
                        "appointmentDate"
                )
        );

        LocalDate today = LocalDate.now();

        Specification<Appointment> spec =
                AppointmentSpecification.doctor(username);

        if (status != null) {
            spec = spec.and(
                    AppointmentSpecification.status(status)
            );
        }

        if (period == AppointmentPeriod.TODAY) {

            spec = spec
                    .and(AppointmentSpecification.fromDate(today))
                    .and(AppointmentSpecification.toDate(today));

        } else if (period == AppointmentPeriod.UPCOMING) {

            spec = spec.and(
                    AppointmentSpecification.fromDate(
                            today.plusDays(1)
                    )
            );

        } else if (period == AppointmentPeriod.PAST) {

            spec = spec.and(
                    AppointmentSpecification.toDate(
                            today.minusDays(1)
                    )
            );
        }

        Page<Appointment> appointments =
                appointmentRepository.findAll(
                        spec,
                        pageable
                );

        return appointments.map(
                appointmentEntityToDto::mapAppointmentEntityToDto
        );
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
                        new ResourceNotFoundException(DOCTOR_NOT_FOUND));

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
                        new ResourceNotFoundException(DOCTOR_NOT_FOUND));
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
                                   SubmitPrescriptionDto dto) {

        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(DOCTOR_NOT_FOUND));

        Appointment appointment = appointmentRepository
                .findById(dto.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            log.warn("Unauthorized prescription attempt by doctor '{}' for appointment {}",
                    username, appointment.getId());
            throw new UnauthorizedOperationException(
                    "You are not authorized to prescribe for this appointment.");
        }

        if (appointment.getAppointmentStatus() != AppointmentStatus.COMPLETED) {
            log.warn("Prescription rejected because appointment {} is not completed.",
                    appointment.getId());
            throw new IllegalOperationException(
                    "Prescription can only be submitted after the appointment is completed.");
        }

        if (prescriptionItemRepository.existsByAppointmentId(appointment.getId())) {
            throw new IllegalOperationException(
                    "Prescription has already been submitted for this appointment.");
        }

        double medicineFee = 0;

        for (PrescriptionItemDto item : dto.medicines()) {
            Medicine medicine = medicineRepository.findById(item.medicineId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Medicine not found: " + item.medicineId()));

            savePrescriptionItem(appointment, medicine, item);

            medicineFee += billingService.calculateMedicineCost(medicine, item.quantity());
        }

        billingService.generateBill(appointment, doctor, medicineFee);
    }

    public void payBill(String username, Long appointmentId, PayBillDto dto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Patient username not found"));

        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Ownership check - same pattern as the fix needed in viewPrescription
        if (!appointment.getPatient().getId().equals(patient.getId())) {
            log.warn("Unauthorized payment attempt by patient '{}' for appointment {}",
                    username, appointmentId);
            throw new UnauthorizedOperationException(
                    "You are not authorized to pay this bill.");
        }

        if (appointment.getPaymentStatus() != PaymentStatus.PENDING) {
            log.warn("Payment rejected for appointment {}: current status is {}",
                    appointmentId, appointment.getPaymentStatus());
            throw new IllegalOperationException(
                    "This bill is not pending payment (current status: "
                            + appointment.getPaymentStatus() + ").");
        }

        appointment.setPaymentMethod(dto.paymentMethod());
        appointment.setPaymentStatus(PaymentStatus.PAID);
        appointmentRepository.save(appointment);

        log.info("Appointment {} marked PAID via {} by patient '{}'",
                appointmentId, dto.paymentMethod(), username);
    }
    private void savePrescriptionItem(Appointment appointment,
                                      Medicine medicine,
                                      PrescriptionItemDto dto) {

        PrescriptionItem item = prescriptionMapper.mapDtoToEntity(dto);

        item.setAppointment(appointment);
        item.setMedicine(medicine);

        prescriptionItemRepository.save(item);
    }



    public PatientAppointmentResponseDto getPatientAppointments(
            String username,
            Integer upcomingPage,
            Integer pastPage,
            Integer size) {

        if (upcomingPage == null || upcomingPage < 0) {
            upcomingPage = 0;
        }

        if (pastPage == null || pastPage < 0) {
            pastPage = 0;
        }

        if (size == null || size <= 0) {
            size = 2;
        }

        // =====================================================
        // TODAY
        // =====================================================

        Pageable todayPageable =
                PageRequest.of(
                        0,
                        100,
                        Sort.by(
                                Sort.Direction.ASC,
                                "appointmentTime"
                        )
                );

        Page<Appointment> todayPage =
                appointmentRepository.findAll(
                        AppointmentSpecification.patient(username)
                                .and(AppointmentSpecification.today()),
                        todayPageable
                );

        List<AppointmentResDto> today =
                todayPage.getContent()
                        .stream()
                        .map(appointmentEntityToDto::mapAppointmentEntityToDto)
                        .toList();


        // =====================================================
        // UPCOMING
        // =====================================================

        Pageable upcomingPageable =
                PageRequest.of(
                        upcomingPage,
                        size,
                        Sort.by(
                                Sort.Direction.ASC,
                                "appointmentDate"
                        ).and(
                                Sort.by(
                                        Sort.Direction.ASC,
                                        "appointmentTime"
                                )
                        )
                );

        Page<Appointment> upcomingPageResult =
                appointmentRepository.findAll(
                        AppointmentSpecification.patient(username)
                                .and(AppointmentSpecification.upcoming()),
                        upcomingPageable
                );

        List<AppointmentResDto> upcoming =
                upcomingPageResult.getContent()
                        .stream()
                        .map(appointmentEntityToDto::mapAppointmentEntityToDto)
                        .toList();


        // =====================================================
        // PAST
        // =====================================================

        Pageable pastPageable =
                PageRequest.of(
                        pastPage,
                        size,
                        Sort.by(
                                Sort.Direction.DESC,
                                "appointmentDate"
                        ).and(
                                Sort.by(
                                        Sort.Direction.DESC,
                                        "appointmentTime"
                                )
                        )
                );

        Page<Appointment> pastPageResult =
                appointmentRepository.findAll(
                        AppointmentSpecification.patient(username)
                                .and(AppointmentSpecification.past()),
                        pastPageable
                );

        List<AppointmentResDto> past =
                pastPageResult.getContent()
                        .stream()
                        .map(appointmentEntityToDto::mapAppointmentEntityToDto)
                        .toList();


        // =====================================================
        // FINAL RESPONSE
        // =====================================================

        return new PatientAppointmentResponseDto(
                today,

                upcoming,
                upcomingPageResult.getTotalElements(),
                upcomingPageResult.getTotalPages(),
                upcomingPage,

                past,
                pastPageResult.getTotalElements(),
                pastPageResult.getTotalPages(),
                pastPage
        );
    }

}
