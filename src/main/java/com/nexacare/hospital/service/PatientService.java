package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.request.PatientProfileDto;
import com.nexacare.hospital.dto.request.PatientRegisterDto;
import com.nexacare.hospital.dto.request.UserLoginDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class PatientService {
    private  final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private  final PatientEntityMapper patientEntityMapper;
    private  final PasswordEncoder passwordEncoder;

private  final JwtService jwtService;
    public TokenDto loginPatient(PatientRegisterDto patientRegisterDto) {
        return jwtService.generateToken(patientRegisterDto);
    }

    public void registerPatient(PatientRegisterDto patientDto) {
        Patient patient=new Patient();
        User user=new User();
        //setting username,password and role
        user.setUsername(patientDto.username());
        user.setPassword(passwordEncoder.encode(patientDto.password()));
        user.setRole(Role.PATIENT);
        user =userRepository.save(user);
        //save the user
        patient.setUser(user);
        //attach the user to the doctor
        patientRepository.save(patient);





    }

    public void updateProfile(PatientProfileDto patientProfileDto, String username) {
        User user=userRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("Patient UserName not found"));
         Patient patient=patientRepository.findByUserId(user.getId()).orElseThrow(()-> new ResourceNotFoundException("Patient Id not found"));
               patient= PatientDtoMapper.mapDtoToPatient(patientProfileDto,patient);
         patientRepository.save(patient);
    }

    public List<PatientResDto> getAllPatient(int page,int size) {
        Pageable pageable=PageRequest.of(page,size);
        List<Patient> patientList=patientRepository.findAllExceptDeactivePatient(pageable).getContent();
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

        return patientEntityMapper.mapPatientEntityToDto(patient);
    }

    public void deActivatePatient(String username) {
        User user =userRepository.findByUserUsername(username)
                        .orElseThrow(()->new ResourceNotFoundException("User nameName not found"));
       user.setActive(false);
        userRepository.save(user);
    }



}
