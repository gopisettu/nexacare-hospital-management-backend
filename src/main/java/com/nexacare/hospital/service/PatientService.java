package com.nexacare.hospital.service;

import com.nexacare.hospital.controller.PatientRegisterByAdminDto;
import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.request.PatientProfileDto;
import com.nexacare.hospital.dto.request.UploadDto;
import com.nexacare.hospital.dto.response.PatientAdminResDto;
import com.nexacare.hospital.dto.response.PatientResDto;
import com.nexacare.hospital.dto.response.TokenDto;
import com.nexacare.hospital.enums.*;
import com.nexacare.hospital.exception.ResourceNotFoundException;
import com.nexacare.hospital.mapper.dtotoentity.PatientDtoMapper;
import com.nexacare.hospital.mapper.entitytodto.PatientEntityMapper;
import com.nexacare.hospital.model.Appointment;
import com.nexacare.hospital.model.Patient;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.AppointmentRepository;
import com.nexacare.hospital.repositories.PatientRepository;
import com.nexacare.hospital.repositories.UserRepository;
import com.nexacare.hospital.utility.UploadUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j

public class PatientService {
    private  final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private  final PatientEntityMapper patientEntityMapper;
    private  final PasswordEncoder passwordEncoder;
    private  final AppointmentRepository appointmentRepository;
    private final String uploadPath="D:/CapestoneProjectHexaware/FinalHospitalManagement/frontend-hospitalmanagement/public/ProductImages";

    private  final UploadUtility uploadUtility;
private  final JwtService jwtService;
    public TokenDto loginPatient(LoginDto loginDto) {

        log.info("Patient '{}' logged in successfully.", loginDto.username());
        return jwtService.generateToken(loginDto.username());
    }

    public void registerPatient(LoginDto loginDto) {
        Patient patient=new Patient();
        User user=new User();
        //setting username,password and role
        user.setUsername(loginDto.username());
        user.setPassword(passwordEncoder.encode(loginDto.password()));
        user.setRole(Role.PATIENT);
        user =userRepository.save(user);
        //save the user
        patient.setUser(user);
        //attach the user to the doctor
        patientRepository.save(patient);
        log.info("Patient registered successfully. User ID: {}, Patient ID: {}",
                user.getId(),
                patient.getId());





    }

    public void updateProfile(PatientProfileDto patientProfileDto, String username) {
        User user=userRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("Patient UserName not found"));
         Patient patient=patientRepository.findByUserId(user.getId()).orElseThrow(()-> new ResourceNotFoundException("Patient Id not found"));
               patient= PatientDtoMapper.mapDtoToPatient(patientProfileDto,patient);
         patientRepository.save(patient);
        patientRepository.save(patient);

        log.info("Patient '{}' updated profile successfully.", username);
    }
    public List<PatientAdminResDto> getAllPatient(int page, int size, String search, String gender,
                                                  String bloodGroup, String appointmentFilter, String sortOption) {

        Specification<Patient> spec = PatientSpecification.filterPatients(search, gender, bloodGroup, appointmentFilter);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        if ("YOUNG".equals(sortOption)) {
            sort = Sort.by(Sort.Direction.DESC, "dob");
        } else if ("OLD".equals(sortOption)) {
            sort = Sort.by(Sort.Direction.ASC, "dob");
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Patient> patientPage = patientRepository.findAll(spec, pageable);
        List<Patient> patientList = patientPage.getContent();

        log.info("Retrieved {} patient(s).", patientList.size());

        List<PatientAdminResDto> patientResDtos = patientList.stream()
                .map(patient -> {
                    Appointment appointment = appointmentRepository.findTopByPatientIdOrderByCreatedAtDesc(patient.getId());
                    return patientEntityMapper.mapPatientAdminRes(patient, appointment);
                })
                .toList();

        return patientResDtos;
    }

    public PatientResDto getPatientByUsername(String username) {

        User user = userRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Username not found"));

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));

        log.info("Patient profile retrieved successfully for '{}'.", username);
        return patientEntityMapper.mapPatientEntityToDto(patient);
    }

    public void deActivatePatient(String username) {
        User user =userRepository.findByUserUsername(username)
                        .orElseThrow(()->new ResourceNotFoundException("User nameName not found"));
       user.setActive(false);
        userRepository.save(user);
        userRepository.save(user);

        log.info("Patient '{}' deactivated successfully.", username);
    }


    public void registerFullPatientByAdmin(PatientRegisterByAdminDto patientRegisterByAdminDto) {
        User user=new User();
        user.setRole(Role.PATIENT);
        user.setUsername(patientRegisterByAdminDto.getUsername());
        user.setPassword(patientRegisterByAdminDto.getPassword());
        userRepository.save(user);
         Patient patient=new Patient();
         patient.setUser(user);
         patient.setFirstName(patientRegisterByAdminDto.getFirstName());
         patient.setLastName(patientRegisterByAdminDto.getLastName());
         patient.setGender(patientRegisterByAdminDto.getGender());
         patient.setDob(patientRegisterByAdminDto.getDob());
         patient.setAadharNumber(patientRegisterByAdminDto.getAadharNumber());
         patient.setBloodGroup(patientRegisterByAdminDto.getBloodGroup());
         patient.setPhone(patientRegisterByAdminDto.getPhone());
patient.setEmail(patientRegisterByAdminDto.getEmail());
patient.setAddress(patientRegisterByAdminDto.getAddress());
    patient.setAllergies(patientRegisterByAdminDto.getAllergies());
    patient.setChronicDisease(patientRegisterByAdminDto.getChronicDisease());

    patientRepository.save(patient);

    }


    public UploadDto uploadImage(long pid, MultipartFile imageFile) throws IOException {
        Patient product=patientRepository.findById(pid)
                .orElseThrow(()->new ResourceNotFoundException("Product Id not found"));

        uploadUtility.validateImage(imageFile);
        Path uPathDir =  Paths.get(uploadPath);
        Path filePath =  uPathDir.resolve(Objects.requireNonNull(imageFile.getOriginalFilename()));

        Files.copy(imageFile.getInputStream(), filePath , StandardCopyOption.REPLACE_EXISTING);

        product.setImageUrl(filePath.toString());

        product = patientRepository.save(product);

        return new UploadDto(
                product.getId(),
                product.getImageUrl(),
                imageFile.getOriginalFilename(),
                "File upload success"
        );

    }

}
