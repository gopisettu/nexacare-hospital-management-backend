package com.nexacare.hospital.controller;

import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.request.UploadDto;
import com.nexacare.hospital.dto.response.TokenDto;
import com.nexacare.hospital.service.AdminService;
import com.nexacare.hospital.service.DoctorService;
import com.nexacare.hospital.service.PatientService;
import com.nexacare.hospital.service.StaffService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private  final DoctorService doctorService;
    private  final StaffService staffService;
    private final AdminService adminService;
    private final PatientService patientService;

    @PostMapping("/loginAdmin")
    public TokenDto loginAdmin(@RequestBody
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
@PostMapping("/addPatient-ByAdmin")
    public  void  registerFullPatient(@RequestBody PatientRegisterByAdminDto patientRegisterByAdminDto){
patientService.registerFullPatientByAdmin(patientRegisterByAdminDto);
}

    @PutMapping("/image/upload/{patientId}")
    public UploadDto uploadImage(@PathVariable long patientId,
                                 @RequestParam("pImage") MultipartFile pImage) throws IOException {
        return  patientService.uploadImage(patientId,pImage);
    }

}
