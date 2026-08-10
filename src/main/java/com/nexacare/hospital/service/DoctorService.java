package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.request.doctorreq.DoctorFilterRequest;
import com.nexacare.hospital.dto.request.doctorreq.DoctorProfileDto;
import com.nexacare.hospital.dto.request.adminreq.DoctorRegisterByAdminDto;
import com.nexacare.hospital.dto.request.authreq.LoginDto;
import com.nexacare.hospital.dto.response.adminres.DoctorAdminResDto;
import com.nexacare.hospital.dto.response.doctorres.AppointmentResDto;
import com.nexacare.hospital.dto.response.doctorres.DoctorDashboardDto;
import com.nexacare.hospital.dto.response.doctorres.DoctorResDto;
import com.nexacare.hospital.dto.response.authres.TokenDto;
import com.nexacare.hospital.enums.AppointmentStatus;
import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.enums.Specialization;
import com.nexacare.hospital.exception.IllegalOperationException;
import com.nexacare.hospital.exception.ResourceNotFoundException;
import com.nexacare.hospital.mapper.dtotoentity.DoctorMapper;
import com.nexacare.hospital.mapper.entitytodto.AppointmentEntityToDto;
import com.nexacare.hospital.mapper.entitytodto.DoctorDtoMapper;
import com.nexacare.hospital.model.Appointment;
import com.nexacare.hospital.model.Doctor;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.AppointmentRepository;
import com.nexacare.hospital.repositories.DoctorRepository;
import com.nexacare.hospital.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private  final DoctorDtoMapper doctorDtoMapper;
    private final AppointmentEntityToDto appointmentEntityToDto;
    private  final JwtService jwtService;
    private  final PasswordEncoder passwordEncoder;
    private  final  DoctorMapper doctorMapper;
    private final AppointmentRepository appointmentRepository;
    private static final String DOCTOR_NOT_FOUND = "Doctor not found";
    private static final String DOCTOR_USER_NOT_FOUND="Doctor username not found";


    public TokenDto loginDoctor(LoginDto loginDto) {
        log.info("Doctor '{}' logged in successfully.", loginDto.username());
        return jwtService.generateToken(loginDto.username(), Role.DOCTOR.toString());
    }
    public void registerDoctor(@Valid LoginDto loginDto) {

        Doctor doctor = new Doctor();
        User user = new User();

        user.setUsername(loginDto.username());
        user.setPassword(passwordEncoder.encode(loginDto.password()));
        user.setRole(Role.DOCTOR);

        user = userRepository.save(user);



        doctor.setUser(user);

        doctorRepository.save(doctor);
        log.info("Doctor '{}' Registered Successfully",user.getUsername());

    }

    public void updateProfile(@Valid DoctorProfileDto doctorProfileDto,String username) {
        User user= userRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(DOCTOR_NOT_FOUND));
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(DOCTOR_NOT_FOUND));
doctor= DoctorMapper.mapDtotoDoctorEntity(doctorProfileDto,doctor);
doctorRepository.save(doctor);
        log.info("Doctor '{}' updated profile successfully.", username);

    }

    public List<DoctorResDto> getAllDoctor(Integer page,Integer size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable=PageRequest.of(page,size,sort);
      List<Doctor> doctor = doctorRepository.findAllActiveDoctors(pageable).getContent();
        log.info("Retrieved {} doctor(s).", doctor.size());
      return  doctor.
              stream()
              .map((d)->doctorDtoMapper.mapDoctorEntityToDto(d))
              .toList();


    }


    public List<DoctorAdminResDto> getAllDoctorAdmin(
            int page,
            int size,
            DoctorFilterRequest filter
    ) {

        Specification<Doctor> spec =
                DoctorSpecification.filterDoctors(
                        filter.search(),
                        filter.gender(),
                        filter.department(),
                        filter.specialization(),
                        filter.qualification()
                );

        Sort sort = Sort.by(
                Sort.Direction.DESC,
                "createdAt"
        );

        if ("LOW".equals(filter.feeSort())) {
            sort = Sort.by(
                    Sort.Direction.ASC,
                    "consultationFee"
            );
        } else if ("HIGH".equals(filter.feeSort())) {
            sort = Sort.by(
                    Sort.Direction.DESC,
                    "consultationFee"
            );
        }

        if ("MIN".equals(filter.experienceSort())) {
            sort = Sort.by(
                    Sort.Direction.ASC,
                    "totalExperienceYear"
            );
        } else if ("MAX".equals(filter.experienceSort())) {
            sort = Sort.by(
                    Sort.Direction.DESC,
                    "totalExperienceYear"
            );
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Doctor> doctorPage =
                doctorRepository.findAll(spec, pageable);

        List<Doctor> doctorList = doctorPage.getContent();

        log.info("Retrieved {} doctor(s).", doctorList.size());

        return doctorList.stream()
                .map(doctorMapper::mapDoctorAdminRes)
                .toList();
    }


    public DoctorResDto getDoctorByUsername(String username) {
        User user=userRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException(DOCTOR_USER_NOT_FOUND));
        Doctor doctor=doctorRepository.findByUserId(user.getId())
                .orElseThrow(()->new ResourceNotFoundException("Doctor userId not found"));
        log.info("Doctor profile retrieved successfully for '{}'.", username);
        return  doctorDtoMapper.mapDoctorEntityToDto(doctor);

    }

    public void deActivateDoctor(String username) {
        User user=userRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException(DOCTOR_USER_NOT_FOUND));
        user.setActive(false);
        userRepository.save(user);
        log.info("Doctor '{}' deactivated successfully.", username);
    }


    public List<DoctorResDto> searchDoctorBySpecialization(String username, Specialization specialization) {
      userRepository.findByUsername(username)
                 .orElseThrow(()->new ResourceNotFoundException(DOCTOR_USER_NOT_FOUND));
        List<Doctor> doctorList=doctorRepository.searchDoctorBySpecialization(specialization);
        if (doctorList.isEmpty()) {
            log.warn("No doctors found for specialization '{}'.", specialization);
            throw new ResourceNotFoundException(
                    "No doctors found for specialization: " + specialization);
        }
        log.info("Found {} doctor(s) for specialization '{}'.",
                doctorList.size(),
                specialization);
         return  doctorList
                .stream()
                .map((d)->doctorDtoMapper.mapDoctorEntityToDto(d))
                .toList();
    }

    public List<DoctorResDto> searchDoctorByDepartment(String username, Department department)
    {
    userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException(DOCTOR_USER_NOT_FOUND));

        List<Doctor> doctorList=doctorRepository.searchDoctorByDepartment(department);
        if (doctorList.isEmpty()) {
            log.warn("No doctors found in department '{}'.", department);
            throw new ResourceNotFoundException(
                    "No doctors found for Department: " + department);
        }
        log.info("Found {} doctor(s) in department '{}'.",
                doctorList.size(),
                department);
        return  doctorList
                .stream()
                .map((d)->doctorDtoMapper.mapDoctorEntityToDto(d))
                .toList();
    }


    public void registerDoctorByAdmin(DoctorRegisterByAdminDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalOperationException("Username already exists.");
        }

        if (doctorRepository.existsByPhone(dto.phone())) {
            throw new IllegalOperationException("Phone number already exists.");
        }

        if (doctorRepository.existsByEmail(dto.email())) {
            throw new IllegalOperationException("Email already exists.");
        }

        User user = new User();
        user.setRole(Role.DOCTOR);
        user.setUsername(dto.username());
        user.setPassword(dto.password());

        userRepository.save(user);

        Doctor doctor = new Doctor();

        doctor.setUser(user);
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

        doctorRepository.save(doctor);
    }

    public void updateDoctorByAdmin(
            String username,
            DoctorProfileDto dto
    ) {

        Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException(DOCTOR_NOT_FOUND));

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

        doctorRepository.save(doctor);
    }

    public DoctorDashboardDto getDoctorDashboard(String username) {

        User user = userRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(DOCTOR_USER_NOT_FOUND));

        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(DOCTOR_NOT_FOUND));

        DoctorResDto doctorDto =
                doctorDtoMapper.mapDoctorEntityToDto(doctor);

        List<Appointment> appointments =
                appointmentRepository.findByDoctorId(doctor.getId());

        LocalDate today = LocalDate.now();

        long totalAppointments = appointments.size();

        long todayAppointments =
                appointments.stream()
                        .filter(a -> a.getAppointmentDate().equals(today))
                        .count();

        long completedAppointments =
                appointments.stream()
                        .filter(a ->
                                a.getAppointmentStatus()
                                        == AppointmentStatus.COMPLETED)
                        .count();

        long totalPatients =
                appointments.stream()
                        .map(a -> a.getPatient().getId())
                        .distinct()
                        .count();

        Map<String, Long> reasonDistribution =
                appointments.stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getReason().name(),
                                Collectors.counting()
                        ));

        List<AppointmentResDto> todayList =
                appointments.stream()
                        .filter(a ->
                                a.getAppointmentDate().equals(today))
                        .sorted(Comparator.comparing(
                                Appointment::getAppointmentTime))
                        .map(appointmentEntityToDto::mapAppointmentEntityToDto)
                        .toList();

        List<AppointmentResDto> upcoming =
                appointments.stream()
                        .filter(a ->
                                a.getAppointmentDate().isAfter(today))
                        .sorted(Comparator.comparing(
                                Appointment::getAppointmentDate))
                        .limit(5)
                        .map(appointmentEntityToDto::mapAppointmentEntityToDto)
                        .toList();

        return new DoctorDashboardDto(

                doctorDto,

                totalPatients,
                totalAppointments,
                todayAppointments,
                completedAppointments,

                reasonDistribution,

                todayList,

                upcoming
        );
    }
}
