package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.request.DoctorProfileDto;
import com.nexacare.hospital.dto.request.DoctorRegisterDto;
import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.response.AppointmentResDto;
import com.nexacare.hospital.dto.response.DoctorResDto;
import com.nexacare.hospital.dto.response.TokenDto;
import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.enums.Specialization;
import com.nexacare.hospital.exception.ResourceNotFoundException;
import com.nexacare.hospital.mapper.dtotoentity.DoctorMapper;
import com.nexacare.hospital.mapper.entitytodto.AppointmentEntityToDto;
import com.nexacare.hospital.mapper.entitytodto.DoctorDtoMapper;
import com.nexacare.hospital.model.Appointment;
import com.nexacare.hospital.model.Doctor;
import com.nexacare.hospital.model.Patient;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.DoctorRepository;
import com.nexacare.hospital.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private  final DoctorDtoMapper doctorDtoMapper;
    private final AppointmentEntityToDto appointmentEntityToDto;
    private  final JwtService jwtService;
    private  final PasswordEncoder passwordEncoder;

    public TokenDto loginDoctor(LoginDto loginDto) {
        return jwtService.generateToken(loginDto.username());
    }
    public void registerDoctor(@Valid DoctorRegisterDto doctorRegisterDto) {

        Doctor doctor = new Doctor();
        User user = new User();

        user.setUsername(doctorRegisterDto.username());
        user.setPassword(passwordEncoder.encode(doctorRegisterDto.password()));
        user.setRole(Role.DOCTOR);

        user = userRepository.save(user);



        doctor.setUser(user);

        doctorRepository.save(doctor);

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

    }

    public List<DoctorResDto> getAllDoctor(Integer page,Integer size) {
        Pageable pageable=PageRequest.of(page,size);
      List<Doctor> doctor = doctorRepository.findAll(pageable).getContent();
      return  doctor.
              stream()
              .map((d)->doctorDtoMapper.mapDoctorEntityToDto(d))
              .toList();

    }

    public DoctorResDto getDoctorByUsername(String username) {
        User user=userRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("Doctor Username Not found"));
        Doctor doctor=doctorRepository.findByUserId(user.getId())
                .orElseThrow(()->new ResourceNotFoundException("Doctor userId not found"));
        return  doctorDtoMapper.mapDoctorEntityToDto(doctor);

    }

    public void deActivateDoctor(String username) {
        User user=userRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("Doctor UserName not found"));
        user.setActive(false);
        userRepository.save(user);
    }


    public List<DoctorResDto> searchDoctorBySpecialization(String username, Specialization specialization) {
         User user =userRepository.findByUsername(username)
                 .orElseThrow(()->new ResourceNotFoundException("Doctor username not found"));
        List<Doctor> doctorList=doctorRepository.searchDoctorBySpecialization(specialization);
        if (doctorList.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No doctors found for specialization: " + specialization);
        }
         return  doctorList
                .stream()
                .map((d)->doctorDtoMapper.mapDoctorEntityToDto(d))
                .toList();
    }

    public List<DoctorResDto> searchDoctorByDepartment(String username, Department department)
    {
        User user =userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("Doctor username not found"));

        List<Doctor> doctorList=doctorRepository.searchDoctorByDepartment(department);
        if (doctorList.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No doctors found for Department: " + department);
        }
        return  doctorList
                .stream()
                .map((d)->doctorDtoMapper.mapDoctorEntityToDto(d))
                .toList();
    }


}
