package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.request.DoctorReq.DoctorProfileDto;
import com.nexacare.hospital.dto.request.AdminReq.DoctorRegisterByAdminDto;
import com.nexacare.hospital.dto.request.AuthReq.LoginDto;
import com.nexacare.hospital.dto.response.AdminRes.DoctorAdminResDto;
import com.nexacare.hospital.dto.response.DoctorRes.DoctorResDto;
import com.nexacare.hospital.dto.response.AuthRes.TokenDto;
import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.enums.Specialization;
import com.nexacare.hospital.exception.IllegalOperationException;
import com.nexacare.hospital.exception.ResourceNotFoundException;
import com.nexacare.hospital.mapper.dtotoentity.DoctorMapper;
import com.nexacare.hospital.mapper.entitytodto.AppointmentEntityToDto;
import com.nexacare.hospital.mapper.entitytodto.DoctorDtoMapper;
import com.nexacare.hospital.model.Doctor;
import com.nexacare.hospital.model.User;
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

import java.util.List;

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

    public TokenDto loginDoctor(LoginDto loginDto) {
        log.info("Doctor '{}' logged in successfully.", loginDto.username());
        return jwtService.generateToken(loginDto.username());

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
                        new ResourceNotFoundException("Doctor not found"));
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));
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

            String search,
            String gender,
            String department,
            String specialization,
            String qualification,

            String feeSort,
            String experienceSort

    ) {

        Specification<Doctor> spec =
                DoctorSpecification.filterDoctors(
                        search,
                        gender,
                        department,
                        specialization,
                        qualification
                );

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");


        // Consultant Fee Sorting

        if ("LOW".equals(feeSort)) {

            sort = Sort.by(Sort.Direction.ASC, "consultationFee");

        } else if ("HIGH".equals(feeSort)) {

            sort = Sort.by(Sort.Direction.DESC, "consultationFee");
        }

        // Experience Sorting

        if ("MIN".equals(experienceSort)) {

            sort = Sort.by(Sort.Direction.ASC, "totalExperienceYear");

        } else if ("MAX".equals(experienceSort)) {

            sort = Sort.by(Sort.Direction.DESC, "totalExperienceYear");
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Doctor> doctorPage = doctorRepository.findAll(spec, pageable);

        List<Doctor> doctorList = doctorPage.getContent();

        log.info("Retrieved {} doctor(s).", doctorList.size());

        return doctorList.stream()
                .map(doctorMapper::mapDoctorAdminRes)
                .toList();
    }




    public DoctorResDto getDoctorByUsername(String username) {
        User user=userRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("Doctor Username Not found"));
        Doctor doctor=doctorRepository.findByUserId(user.getId())
                .orElseThrow(()->new ResourceNotFoundException("Doctor userId not found"));
        log.info("Doctor profile retrieved successfully for '{}'.", username);
        return  doctorDtoMapper.mapDoctorEntityToDto(doctor);

    }

    public void deActivateDoctor(String username) {
        User user=userRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("Doctor UserName not found"));
        user.setActive(false);
        userRepository.save(user);
        log.info("Doctor '{}' deactivated successfully.", username);
    }


    public List<DoctorResDto> searchDoctorBySpecialization(String username, Specialization specialization) {
      userRepository.findByUsername(username)
                 .orElseThrow(()->new ResourceNotFoundException("Doctor username not found"));
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
                .orElseThrow(()->new ResourceNotFoundException("Doctor username not found"));

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
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

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
}
