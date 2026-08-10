package com.nexacare.hospital.controller;

import com.nexacare.hospital.dto.request.authreq.LoginDto;
import com.nexacare.hospital.service.AdminService;
import com.nexacare.hospital.service.DoctorService;
import com.nexacare.hospital.service.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/executive")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ExecutiveController {
    private final AdminService adminService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @PostMapping("/register-AdminByExecutive")
    public  void registerAdmin(@Valid @RequestBody LoginDto loginDto)
    {
        adminService.registerAdmin(loginDto);
    }


    @PutMapping("/deActivateDoctor-ByExecutive/{username}")
    public void deActiveDoctor(@PathVariable String username)
    {
        doctorService.deActivateDoctor(username);
    }
    @PutMapping("/deActivatePatient-ByExecutive/{username}")
    public void deActivatePatient(@PathVariable String username){
        patientService.deActivatePatient(username);
    }


}
