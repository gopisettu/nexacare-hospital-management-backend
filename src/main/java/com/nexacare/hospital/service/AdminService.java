package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.response.TokenDto;
import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.model.User;
import com.nexacare.hospital.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {
    private  final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;

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
}
