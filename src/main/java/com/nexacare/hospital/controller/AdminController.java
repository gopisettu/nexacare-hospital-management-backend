package com.nexacare.hospital.controller;

import com.nexacare.hospital.dto.request.DoctorReq.DoctorProfileDto;
import com.nexacare.hospital.dto.request.AdminReq.DoctorRegisterByAdminDto;
import com.nexacare.hospital.dto.request.AuthReq.LoginDto;
import com.nexacare.hospital.dto.request.UploadDto;
import com.nexacare.hospital.dto.response.AdminRes.DashboardAllResDto;
import com.nexacare.hospital.dto.response.AdminRes.DoctorAdminResDto;
import com.nexacare.hospital.dto.response.AdminRes.MedicineAdminRes;
import com.nexacare.hospital.dto.response.PatientRegisterByAdminDto;
import com.nexacare.hospital.dto.response.AuthRes.TokenDto;
import com.nexacare.hospital.enums.BatchStatus;
import com.nexacare.hospital.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private  final DoctorService doctorService;
    private  final StaffService staffService;
    private final AdminService adminService;
    private final PatientService patientService;
    private final MedicineService medicineService;

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
    @PostMapping("/addDoctor-ByAdmin")
    public void registerDoctorByAdmin(
            @RequestBody DoctorRegisterByAdminDto doctorRegisterByAdminDto
    ) {
        doctorService.registerDoctorByAdmin(doctorRegisterByAdminDto);
    }
    @PutMapping("/updateDoctor-ByAdmin/{username}")
    public void updateDoctorByAdmin(
            @PathVariable String username,
            @RequestBody DoctorProfileDto doctorProfileDto
    ) {
        doctorService.updateDoctorByAdmin(username, doctorProfileDto);
    }

@PutMapping("/doctorimage/upload/{doctorId}")
public UploadDto uploadImageDoctor(@PathVariable long doctorId,
                             @RequestParam("pImage") MultipartFile pImage) throws IOException {
    return  patientService.uploadImageDoctor(doctorId,pImage);
}

    @PutMapping("/image/upload/{patientId}")
    public UploadDto uploadImage(@PathVariable long patientId,
                                 @RequestParam("pImage") MultipartFile pImage) throws IOException {
        return  patientService.uploadImage(patientId,pImage);
    }


   @GetMapping("/admin-dashboardAllRequiredData")
    public DashboardAllResDto getAdminAllDashData(){
        return adminService.getAdminAllDashData();

   }

    @GetMapping("/get-allDoctor")
    public List<DoctorAdminResDto> getAllDoctor(

            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "8") Integer size,

            @RequestParam(required = false) String search,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String qualification,

            @RequestParam(required = false) String feeSort,
            @RequestParam(required = false) String experienceSort
    ) {

        return doctorService.getAllDoctorAdmin(
                page,
                size,
                search,
                gender,
                department,
                specialization,
                qualification,
                feeSort,
                experienceSort
        );
    }

    @GetMapping("/get-allMedicines")
    public Page<MedicineAdminRes> getAllMedicines(

            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "8") Integer size,

            @RequestParam(required = false) String search,

            @RequestParam(required = false) String category,

            @RequestParam(required = false) BatchStatus batchStatus,

            @RequestParam(required = false) String sortOption
    ) {

        return medicineService.getAllMedicines(
                page,
                size,
                search,
                category,
                batchStatus,
                sortOption
        );

    }
}
