package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.request.AuthReq.LoginDto;
import com.nexacare.hospital.dto.response.AdminRes.DashboardAllResDto;
import com.nexacare.hospital.dto.response.AdminRes.DashboardStatsDTO;
import com.nexacare.hospital.dto.response.AdminRes.MedicineResDto;
import com.nexacare.hospital.dto.response.DoctorRes.AppointmentResDto;
import com.nexacare.hospital.dto.response.AuthRes.TokenDto;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.mapper.entitytodto.AppointmentEntityToDto;
import com.nexacare.hospital.mapper.entitytodto.MedicineEntityToDto;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {
    private  final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicineRepository medicineRepository;

    private final AppointmentEntityToDto appointmentEntityToDto;
    private  final MedicineEntityToDto medicineEntityToDto;

    public void registerAdmin(@Valid LoginDto loginDto) {
        User user=new User();
        user.setUsername(loginDto.username());
        user.setPassword(passwordEncoder.encode(loginDto.password()));
        user.setRole(Role.ADMIN);
        user.setActive(true);
        userRepository.save(user);

    }


    public TokenDto loginAdmin(LoginDto loginDto) {
        return jwtService.generateToken(loginDto.username());
    }



    public DashboardAllResDto getAdminAllDashData() {
        // Statistics
        DashboardStatsDTO stats = new DashboardStatsDTO(
                patientRepository.count(),
                doctorRepository.count(),
                appointmentRepository.count()
        );

        // Today's appointments
        List<AppointmentResDto> todayAppointments =
                appointmentRepository.findByAppointmentDate(LocalDate.now())
                        .stream()
                        .map(appointment -> appointmentEntityToDto.mapAppointmentEntityToDto(appointment))
                        .toList();
        // Medicines
        List<MedicineResDto> medicines =
                medicineRepository.findTop5ByOrderByCreatedAtDesc()
                        .stream()
                        .map(medicineEntityToDto::toMedicineResDto)
                        .toList();

        // Reason Distribution
        Map<String, Long> reasonDistribution =
                appointmentRepository.findAll()
                        .stream()
                        .collect(Collectors.groupingBy(
                                a -> a.getReason().name(),
                                Collectors.counting()
                        ));

        return new DashboardAllResDto(
                stats,
                reasonDistribution,
                todayAppointments,
                medicines
        );
    }
}
