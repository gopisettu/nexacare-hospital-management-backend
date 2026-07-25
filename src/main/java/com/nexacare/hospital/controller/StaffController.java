package com.nexacare.hospital.controller;

import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.response.TokenDto;
import com.nexacare.hospital.service.StaffService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff")
@AllArgsConstructor
public class StaffController {
    private final StaffService staffService;
    @PostMapping("/loginStaff")
    public TokenDto loginStaff(@RequestBody
                                 LoginDto loginDto){
        return staffService.loginStaff(loginDto);
    }
}
