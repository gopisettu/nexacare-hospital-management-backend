package com.nexacare.hospital.service;

import com.nexacare.hospital.controller.PatientRegisterByAdminDto;
import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.request.PatientProfileDto;
import com.nexacare.hospital.dto.response.PatientResDto;
import com.nexacare.hospital.dto.response.TokenDto;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.exception.ResourceNotFoundException;
import com.nexacare.hospital.mapper.dtotoentity.PatientDtoMapper;
import com.nexacare.hospital.mapper.entitytodto.PatientEntityMapper;
import com.nexacare.hospital.model.Patient;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.PatientRepository;
import com.nexacare.hospital.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j

public class PatientService {
    private  final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private  final PatientEntityMapper patientEntityMapper;
    private  final PasswordEncoder passwordEncoder;

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

    public List<PatientResDto> getAllPatient(int page,int size) {
       Sort sort= Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable=PageRequest.of(page,size,sort);
        List<Patient> patientList=patientRepository.findAllExceptDeactivePatient(pageable).getContent();
        log.info("Retrieved {} patient(s).", patientList.size());
         return  patientList.stream()
                .map((p)->patientEntityMapper.mapPatientEntityToDto(p))
                .toList();

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

}
