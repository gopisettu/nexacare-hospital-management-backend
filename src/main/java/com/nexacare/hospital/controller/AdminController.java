package com.nexacare.hospital.controller;

import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.response.TokenDto;
import com.nexacare.hospital.service.AdminService;
import com.nexacare.hospital.service.DoctorService;
import com.nexacare.hospital.service.StaffService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {
    private  final DoctorService doctorService;
    private  final StaffService staffService;
    private final AdminService adminService;

    @PostMapping("/register-SelfAdmin")
    public  void registerAdmin(@Valid @RequestBody LoginDto loginDto)
    {
        adminService.registerAdmin(loginDto);
    }
    @PostMapping("/loginAdmin")
    public TokenDto loginPatient(@RequestBody
                                 LoginDto loginDto){
        return adminService.loginAdmin(loginDto);
    }
    @PostMapping("/register-doctorByAdmin")
    public void registerDoctor( @Valid @RequestBody LoginDto loginDto){
        doctorService.registerDoctor(loginDto);
    }
    @PostMapping("/register-staffByAdmin")
    public  void registerStaff(@Valid @RequestBody LoginDto loginDto)
    {
     staffService.registerStaff(loginDto);
    }


}
