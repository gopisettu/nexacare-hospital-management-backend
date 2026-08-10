package com.nexacare.hospital.Service;


import com.nexacare.hospital.dto.request.authreq.LoginDto;
import com.nexacare.hospital.dto.request.patientreq.PatientProfileDto;
import com.nexacare.hospital.dto.response.adminres.PatientAdminResDto;
import com.nexacare.hospital.dto.response.patientres.PatientResDto;
import com.nexacare.hospital.enums.BloodGroup;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.mapper.entitytodto.PatientEntityMapper;
import com.nexacare.hospital.model.Appointment;
import com.nexacare.hospital.model.Patient;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.AppointmentRepository;
import com.nexacare.hospital.repositories.DoctorRepository;
import com.nexacare.hospital.repositories.PatientRepository;
import com.nexacare.hospital.repositories.UserRepository;
import com.nexacare.hospital.service.JwtService;
import com.nexacare.hospital.service.PatientService;
import com.nexacare.hospital.utility.UploadUtility;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientEntityMapper patientEntityMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UploadUtility uploadUtility;

    @Mock
    private JwtService jwtService;

    private Patient patient1;
    private Patient patient2;

    private User user1;
    private User user2;

    @BeforeEach
    public void init() {

        user1 = new User();
        user1.setId(1L);
        user1.setUsername("patient1@gmail.com");
        user1.setPassword("patient123");
        user1.setRole(Role.PATIENT);
        user1.setActive(true);

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("patient2@gmail.com");
        user2.setPassword("patient123");
        user2.setRole(Role.PATIENT);
        user2.setActive(true);


        patient1 = new Patient();

        patient1.setId(1L);
        patient1.setUser(user1);
        patient1.setFirstName("Gopi");
        patient1.setLastName("Settu");
        patient1.setGender(Gender.MALE);
        patient1.setDob(
                LocalDate.of(2000, 5, 10)
        );
        patient1.setAadharNumber(
                "123456789012"
        );
        patient1.setBloodGroup(
                BloodGroup.O_POSITIVE
        );
        patient1.setPhone(
                "9876543210"
        );
        patient1.setEmail(
                "patient1@gmail.com"
        );
        patient1.setAddress(
                "Chennai"
        );
        patient1.setAllergies(
                "Dust"
        );
        patient1.setChronicDisease(
                "None"
        );


        patient2 = new Patient();

        patient2.setId(2L);
        patient2.setUser(user2);
        patient2.setFirstName("Arun");
        patient2.setLastName("Kumar");
        patient2.setGender(Gender.MALE);
        patient2.setDob(
                LocalDate.of(1998, 4, 15)
        );
        patient2.setAadharNumber(
                "123456789013"
        );
        patient2.setBloodGroup(
                BloodGroup.A_POSITIVE
        );
        patient2.setPhone(
                "9876543211"
        );
        patient2.setEmail(
                "patient2@gmail.com"
        );
        patient2.setAddress(
                "Bangalore"
        );
        patient2.setAllergies(
                "None"
        );
        patient2.setChronicDisease(
                "None"
        );
    }


    // ADD / REGISTER PATIENT

    @Test
    public void registerPatientTest() {

        LoginDto dto =
                new LoginDto(
                        "patient@gmail.com",
                        "patient123"
                );

        when(passwordEncoder.encode("patient123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user1);

        when(patientRepository.save(any(Patient.class)))
                .thenReturn(patient1);

        patientService.registerPatient(dto);

        verify(passwordEncoder)
                .encode("patient123");

        verify(userRepository)
                .save(any(User.class));

        verify(patientRepository)
                .save(any(Patient.class));
    }


    // UPDATE PATIENT

    @Test
    public void updatePatientProfileTest() {

        PatientProfileDto dto =
                new PatientProfileDto(
                        "Gopi",
                        "Settu",
                        Gender.MALE,
                        LocalDate.of(2000, 5, 10),
                        "123456789012",
                        BloodGroup.O_POSITIVE,
                        "9876543210",
                        "patient@gmail.com",
                        "Chennai",
                        "Dust",
                        "None"
                );

        when(userRepository.findByUserUsername(
                "patient1@gmail.com"
        )).thenReturn(Optional.of(user1));

        when(patientRepository.findByUserId(1L))
                .thenReturn(Optional.of(patient1));

        when(patientRepository.save(any(Patient.class)))
                .thenReturn(patient1);

        patientService.updateProfile(
                dto,
                "patient1@gmail.com"
        );

        verify(userRepository)
                .findByUserUsername(
                        "patient1@gmail.com"
                );

        verify(patientRepository)
                .findByUserId(1L);

        verify(patientRepository)
                .save(patient1);
    }


    // GET ALL PATIENTS

    @Test
    public void getAllPatientTest() {

        List<Patient> patients =
                List.of(
                        patient1,
                        patient2
                );

        Page<Patient> page =
                new PageImpl<>(patients);

        when(patientRepository.findAll(
                any(org.springframework.data.jpa.domain.Specification.class),
                any(Pageable.class)
        )).thenReturn(page);


        Appointment appointment1 = new Appointment();

        appointment1.setId(101L);


        when(appointmentRepository
                .findTopByPatientIdOrderByCreatedAtDesc(1L))
                .thenReturn(appointment1);

        when(appointmentRepository
                .findTopByPatientIdOrderByCreatedAtDesc(2L))
                .thenReturn(null);


        PatientAdminResDto dto1 =
                mock(PatientAdminResDto.class);

        PatientAdminResDto dto2 =
                mock(PatientAdminResDto.class);


        when(patientEntityMapper.mapPatientAdminRes(
                patient1,
                appointment1
        )).thenReturn(dto1);

        when(patientEntityMapper.mapPatientAdminRes(
                patient2,
                null
        )).thenReturn(dto2);


        List<PatientAdminResDto> result =
                patientService.getAllPatient(
                        0,
                        10,
                        "",
                        "",
                        "",
                        "",
                        ""
                );


        Assertions.assertNotNull(result);

        Assertions.assertEquals(
                2,
                result.size()
        );

        verify(patientRepository)
                .findAll(
                        any(org.springframework.data.jpa.domain.Specification.class),
                        any(Pageable.class)
                );

        verify(appointmentRepository)
                .findTopByPatientIdOrderByCreatedAtDesc(1L);

        verify(appointmentRepository)
                .findTopByPatientIdOrderByCreatedAtDesc(2L);
    }


    // GET PATIENT BY USERNAME

    @Test
    public void getPatientByUsernameTest() {

        when(userRepository.findByUserUsername(
                "patient1@gmail.com"
        )).thenReturn(Optional.of(user1));

        when(patientRepository.findByUserId(1L))
                .thenReturn(Optional.of(patient1));


        PatientResDto dto =
                new PatientResDto(
                        1L,
                        "patient1@gmail.com",
                        "Gopi",
                        "Settu",
                        Gender.MALE,
                        LocalDate.of(2000, 5, 10),
                        "123456789012",
                        BloodGroup.O_POSITIVE,
                        "9876543210",
                        "patient1@gmail.com",
                        "Chennai",
                        "Dust",
                        "None"
                );


        when(patientEntityMapper.mapPatientEntityToDto(
                patient1
        )).thenReturn(dto);


        PatientResDto result =
                patientService.getPatientByUsername(
                        "patient1@gmail.com"
                );


        Assertions.assertNotNull(result);

        Assertions.assertEquals(
                "Gopi",
                result.firstName()
        );

        Assertions.assertEquals(
                "patient1@gmail.com",
                result.username()
        );


        verify(userRepository)
                .findByUserUsername(
                        "patient1@gmail.com"
                );

        verify(patientRepository)
                .findByUserId(1L);
    }


    // DEACTIVATE PATIENT

    @Test
    public void deactivatePatientTest() {

        when(userRepository.findByUserUsername(
                "patient1@gmail.com"
        )).thenReturn(Optional.of(user1));

        when(userRepository.save(any(User.class)))
                .thenReturn(user1);


        patientService.deActivatePatient(
                "patient1@gmail.com"
        );


        Assertions.assertFalse(
                user1.isActive()
        );


        verify(userRepository)
                .save(user1);
    }
}