package com.nexacare.hospital.Service;



import com.nexacare.hospital.dto.request.authreq.LoginDto;
import com.nexacare.hospital.dto.response.adminres.DashboardAllResDto;
import com.nexacare.hospital.dto.response.doctorres.AppointmentResDto;
import com.nexacare.hospital.dto.response.adminres.MedicineResDto;

import com.nexacare.hospital.enums.Reason;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.mapper.entitytodto.AppointmentEntityToDto;
import com.nexacare.hospital.mapper.entitytodto.MedicineEntityToDto;
import com.nexacare.hospital.model.Appointment;

import com.nexacare.hospital.model.Medicine;

import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.AppointmentRepository;
import com.nexacare.hospital.repositories.DoctorRepository;
import com.nexacare.hospital.repositories.MedicineRepository;
import com.nexacare.hospital.repositories.PatientRepository;
import com.nexacare.hospital.repositories.UserRepository;
import com.nexacare.hospital.service.AdminService;
import com.nexacare.hospital.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private AppointmentEntityToDto appointmentEntityToDto;

    @Mock
    private MedicineEntityToDto medicineEntityToDto;

    private User user1;
    private LoginDto loginDto;

    @BeforeEach
    public void init() {

        loginDto = new LoginDto(
                "admin@gmail.com",
                "admin123"
        );

        user1 = new User();

        user1.setId(1L);
        user1.setUsername("admin@gmail.com");
        user1.setPassword("encodedPassword");
        user1.setRole(Role.ADMIN);
        user1.setActive(true);
    }


    // REGISTER ADMIN

    @Test
    public void registerAdminTest() {

        when(passwordEncoder.encode("admin123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user1);

        adminService.registerAdmin(loginDto);

        verify(passwordEncoder)
                .encode("admin123");

        verify(userRepository)
                .save(any(User.class));
    }




    // ADMIN DASHBOARD

    @Test
    public void getAdminAllDashDataTest() {

        // Patient count

        when(patientRepository.count())
                .thenReturn(10L);

        // Doctor count

        when(doctorRepository.count())
                .thenReturn(5L);

        // Appointment count

        when(appointmentRepository.count())
                .thenReturn(20L);


        // Today's appointments

        Appointment appointment =
                mock(Appointment.class);

        AppointmentResDto appointmentDto =
                mock(AppointmentResDto.class);

        when(appointmentRepository
                .findByAppointmentDate(any()))
                .thenReturn(List.of(appointment));

        when(appointmentEntityToDto
                .mapAppointmentEntityToDto(appointment))
                .thenReturn(appointmentDto);


        // Medicines

        Medicine medicine =
                mock(Medicine.class);

        MedicineResDto medicineDto =
                mock(MedicineResDto.class);

        when(medicineRepository
                .findTop5ByOrderByCreatedAtDesc())
                .thenReturn(List.of(medicine));

        when(medicineEntityToDto
                .toMedicineResDto(medicine))
                .thenReturn(medicineDto);


        // Reason distribution

        Appointment appointment1 =
                mock(Appointment.class);

        Appointment appointment2 =
                mock(Appointment.class);
        when(appointment1.getReason())
                .thenReturn(Reason.GENERAL_CONSULTATION);

        when(appointment2.getReason())
                .thenReturn(Reason.GENERAL_CONSULTATION);
        when(appointmentRepository.findAll())
                .thenReturn(
                        List.of(
                                appointment1,
                                appointment2
                        )
                );


        // Call service

        DashboardAllResDto result =
                adminService.getAdminAllDashData();


        // Assertions

        Assertions.assertNotNull(result);


        // Verify repository calls

        verify(patientRepository)
                .count();

        verify(doctorRepository)
                .count();

        verify(appointmentRepository)
                .count();

        verify(appointmentRepository)
                .findByAppointmentDate(any());

        verify(appointmentRepository)
                .findAll();

        verify(medicineRepository)
                .findTop5ByOrderByCreatedAtDesc();


        // Verify mapper calls

        verify(appointmentEntityToDto)
                .mapAppointmentEntityToDto(appointment);

        verify(medicineEntityToDto)
                .toMedicineResDto(medicine);
    }
}