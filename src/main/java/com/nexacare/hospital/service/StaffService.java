package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.response.TokenDto;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.model.Staff;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.DoctorRepository;
import com.nexacare.hospital.repositories.StaffRepository;
import com.nexacare.hospital.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StaffService {
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;
    private  final StaffRepository staffRepository;
    private  final JwtService jwtService;
    public void registerStaff(@Valid LoginDto loginDto) {
        Staff staff=new Staff();
        User user=new User();
        user.setUsername(loginDto.password());
        user.setPassword(passwordEncoder.encode(loginDto.password()));
        userRepository.save(user);
        staff.setUser(user);
        staff.setFull_name(loginDto.username());
        staff.setRole(Role.STAFF);
        staffRepository.save(staff);
        log.info("Staff Successfully Registedred  ");

    }

    public TokenDto loginStaff(LoginDto loginDto) {
        return  jwtService.generateToken(loginDto.password());
    }


}
