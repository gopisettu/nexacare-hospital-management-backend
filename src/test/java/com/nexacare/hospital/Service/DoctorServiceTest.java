package com.nexacare.hospital.Service;
import com.nexacare.hospital.dto.request.authreq.LoginDto;
import com.nexacare.hospital.dto.request.doctorreq.DoctorProfileDto;
import com.nexacare.hospital.dto.response.doctorres.DoctorResDto;
import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.Qualification;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.enums.Specialization;
import com.nexacare.hospital.mapper.dtotoentity.DoctorMapper;
import com.nexacare.hospital.mapper.entitytodto.DoctorDtoMapper;
import com.nexacare.hospital.mapper.entitytodto.AppointmentEntityToDto;
import com.nexacare.hospital.model.Doctor;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.AppointmentRepository;
import com.nexacare.hospital.repositories.DoctorRepository;
import com.nexacare.hospital.repositories.UserRepository;
import com.nexacare.hospital.service.DoctorService;
import com.nexacare.hospital.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DoctorDtoMapper doctorDtoMapper;

    @Mock
    private AppointmentEntityToDto appointmentEntityToDto;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private AppointmentRepository appointmentRepository;

    private Doctor doctor1;
    private Doctor doctor2;

    private User user1;
    private User user2;

    @BeforeEach
    public void init() {

        user1 = new User();
        user1.setId(1L);
        user1.setUsername("doctor1@gmail.com");
        user1.setPassword("doctor123");
        user1.setRole(Role.DOCTOR);
        user1.setActive(true);

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("doctor2@gmail.com");
        user2.setPassword("doctor123");
        user2.setRole(Role.DOCTOR);
        user2.setActive(true);

        doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setUser(user1);
        doctor1.setFirstName("Gopi");
        doctor1.setLastName("Settu");
        doctor1.setGender(Gender.MALE);
        doctor1.setPhone("9876543210");
        doctor1.setEmail("doctor1@gmail.com");
        doctor1.setAddress("Chennai");
        doctor1.setQualification(Qualification.MBBS);
        doctor1.setDepartment(Department.CARDIOLOGY);
        doctor1.setSpecialization(Specialization.CARDIOLOGIST);
        doctor1.setTotalExperienceYear(5);
        doctor1.setConsultationFee(500.0);

        doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setUser(user2);
        doctor2.setFirstName("Arun");
        doctor2.setLastName("Kumar");
        doctor2.setGender(Gender.MALE);
        doctor2.setPhone("9876543211");
        doctor2.setEmail("doctor2@gmail.com");
        doctor2.setAddress("Bangalore");
        doctor2.setQualification(Qualification.MD);
        doctor2.setDepartment(Department.NEUROLOGY);
        doctor2.setSpecialization(Specialization.NEUROLOGIST);
        doctor2.setTotalExperienceYear(8);
        doctor2.setConsultationFee(800.0);
    }

    // ADD / REGISTER DOCTOR

    @Test
    public void registerDoctorTest() {

        LoginDto dto =
                new LoginDto(
                        "doctor@gmail.com",
                        "doctor123"
                );

        when(passwordEncoder.encode("doctor123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user1);

        when(doctorRepository.save(any(Doctor.class)))
                .thenReturn(doctor1);

        doctorService.registerDoctor(dto);

        verify(passwordEncoder)
                .encode("doctor123");

        verify(userRepository)
                .save(any(User.class));

        verify(doctorRepository)
                .save(any(Doctor.class));
    }


    // UPDATE DOCTOR

    @Test
    public void updateDoctorProfileTest() {

        DoctorProfileDto dto =
                new DoctorProfileDto(
                        "Gopi",
                        "Settu",
                        Gender.MALE,
                        "9876543210",
                        "doctor@gmail.com",
                        "Chennai",
                        Qualification.MBBS,
                        Department.CARDIOLOGY,
                        Specialization.CARDIOLOGIST,
                        6,
                        600.0
                );

        when(userRepository.findByUserUsername("doctor1@gmail.com"))
                .thenReturn(Optional.of(user1));

        when(doctorRepository.findByUserId(1L))
                .thenReturn(Optional.of(doctor1));

        when(doctorRepository.save(any(Doctor.class)))
                .thenReturn(doctor1);

        doctorService.updateProfile(
                dto,
                "doctor1@gmail.com"
        );

        verify(userRepository)
                .findByUserUsername("doctor1@gmail.com");

        verify(doctorRepository)
                .findByUserId(1L);

        verify(doctorRepository)
                .save(doctor1);
    }


    // GET ALL DOCTORS

    @Test
    public void getAllDoctorTest() {

        List<Doctor> doctors =
                List.of(
                        doctor1,
                        doctor2
                );

        Page<Doctor> page =
                new PageImpl<>(doctors);

        when(doctorRepository.findAllActiveDoctors(any(Pageable.class)))
                .thenReturn(page);

        DoctorResDto dto1 =
                new DoctorResDto(
                        1L,
                        "doctor1@gmail.com",
                        "Gopi",
                        "Settu",
                        Gender.MALE,
                        "9876543210",
                        "doctor1@gmail.com",
                        "Chennai",
                        Qualification.MBBS,
                        Department.CARDIOLOGY,
                        Specialization.CARDIOLOGIST,
                        5,
                        500.0
                );

        DoctorResDto dto2 =
                new DoctorResDto(
                        2L,
                        "doctor2@gmail.com",
                        "Arun",
                        "Kumar",
                        Gender.MALE,
                        "9876543211",
                        "doctor2@gmail.com",
                        "Bangalore",
                        Qualification.MD,
                        Department.NEUROLOGY,
                        Specialization.NEUROLOGIST,
                        8,
                        800.0
                );

        when(doctorDtoMapper.mapDoctorEntityToDto(doctor1))
                .thenReturn(dto1);

        when(doctorDtoMapper.mapDoctorEntityToDto(doctor2))
                .thenReturn(dto2);

        List<DoctorResDto> result =
                doctorService.getAllDoctor(0, 10);

        Assertions.assertNotNull(result);

        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals(
                "Gopi",
                result.get(0).firstName()
        );

        verify(doctorRepository)
                .findAllActiveDoctors(any(Pageable.class));
    }


    // GET DOCTOR BY USERNAME

    @Test
    public void getDoctorByUsernameTest() {

        when(userRepository.findByUserUsername("doctor1@gmail.com"))
                .thenReturn(Optional.of(user1));

        when(doctorRepository.findByUserId(1L))
                .thenReturn(Optional.of(doctor1));

        DoctorResDto dto =
                new DoctorResDto(
                        1L,
                        "doctor1@gmail.com",
                        "Gopi",
                        "Settu",
                        Gender.MALE,
                        "9876543210",
                        "doctor1@gmail.com",
                        "Chennai",
                        Qualification.MBBS,
                        Department.CARDIOLOGY,
                        Specialization.CARDIOLOGIST,
                        5,
                        500.0
                );

        when(doctorDtoMapper.mapDoctorEntityToDto(doctor1))
                .thenReturn(dto);

        DoctorResDto result =
                doctorService.getDoctorByUsername(
                        "doctor1@gmail.com"
                );

        Assertions.assertNotNull(result);

        Assertions.assertEquals(
                "Gopi",
                result.firstName()
        );

        Assertions.assertEquals(
                "doctor1@gmail.com",
                result.username()
        );

        verify(userRepository)
                .findByUserUsername("doctor1@gmail.com");

        verify(doctorRepository)
                .findByUserId(1L);
    }


    // DEACTIVATE DOCTOR

    @Test
    public void deactivateDoctorTest() {

        when(userRepository.findByUserUsername("doctor1@gmail.com"))
                .thenReturn(Optional.of(user1));

        when(userRepository.save(any(User.class)))
                .thenReturn(user1);

        doctorService.deActivateDoctor(
                "doctor1@gmail.com"
        );

        Assertions.assertFalse(
                user1.isActive()
        );

        verify(userRepository)
                .save(user1);
    }


    // SEARCH DOCTOR BY SPECIALIZATION

    @Test
    public void searchDoctorBySpecializationTest() {

        when(userRepository.findByUsername("doctor1@gmail.com"))
                .thenReturn(Optional.of(user1));

        when(doctorRepository.searchDoctorBySpecialization(
                Specialization.CARDIOLOGIST
        )).thenReturn(List.of(doctor1));

        DoctorResDto dto =
                new DoctorResDto(
                        1L,
                        "doctor1@gmail.com",
                        "Gopi",
                        "Settu",
                        Gender.MALE,
                        "9876543210",
                        "doctor1@gmail.com",
                        "Chennai",
                        Qualification.MBBS,
                        Department.CARDIOLOGY,
                        Specialization.CARDIOLOGIST,
                        5,
                        500.0
                );

        when(doctorDtoMapper.mapDoctorEntityToDto(doctor1))
                .thenReturn(dto);

        List<DoctorResDto> result =
                doctorService.searchDoctorBySpecialization(
                        "doctor1@gmail.com",
                        Specialization.CARDIOLOGIST
                );

        Assertions.assertEquals(
                1,
                result.size()
        );

        Assertions.assertEquals(
                Specialization.CARDIOLOGIST,
                result.get(0).specialization()
        );

        verify(doctorRepository)
                .searchDoctorBySpecialization(
                        Specialization.CARDIOLOGIST
                );
    }
}