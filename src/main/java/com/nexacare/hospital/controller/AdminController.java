package com.nexacare.hospital.controller;

import com.nexacare.hospital.dto.request.doctorreq.DoctorFilterRequest;
import com.nexacare.hospital.dto.request.doctorreq.DoctorProfileDto;
import com.nexacare.hospital.dto.request.adminreq.DoctorRegisterByAdminDto;
import com.nexacare.hospital.dto.request.authreq.LoginDto;
import com.nexacare.hospital.dto.request.imageuploadreq.UploadDto;
import com.nexacare.hospital.dto.request.patientreq.PatientProfileDto;
import com.nexacare.hospital.dto.response.adminres.DashboardAllResDto;
import com.nexacare.hospital.dto.response.adminres.DoctorAdminResDto;
import com.nexacare.hospital.dto.response.adminres.MedicineAdminRes;
import com.nexacare.hospital.dto.response.PatientRegisterByAdminDto;
import com.nexacare.hospital.dto.response.authres.TokenDto;
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
    public  void  registerFullPatient( @Valid @RequestBody PatientRegisterByAdminDto patientRegisterByAdminDto){
patientService.registerFullPatientByAdmin(patientRegisterByAdminDto);
}

    @PutMapping("/update-patientProfile/{username}")
    public void updateProfile(@Valid @RequestBody PatientProfileDto patientProfileDto, @PathVariable String username){
        patientService.updateProfile(patientProfileDto,username);
    }
    @PostMapping("/addDoctor-ByAdmin")
    public void registerDoctorByAdmin(
            @Valid @RequestBody DoctorRegisterByAdminDto doctorRegisterByAdminDto
    ) {
        doctorService.registerDoctorByAdmin(doctorRegisterByAdminDto);
    }
    @PutMapping("/updateDoctor-ByAdmin/{username}")
    public void updateDoctorByAdmin(
            @PathVariable String username,
            @Valid @RequestBody DoctorProfileDto doctorProfileDto
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

            @RequestParam(required = false, defaultValue = "0")
            Integer page,

            @RequestParam(required = false, defaultValue = "8")
            Integer size,

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            String gender,

            @RequestParam(required = false)
            String department,

            @RequestParam(required = false)
            String specialization,

            @RequestParam(required = false)
            String qualification,

            @RequestParam(required = false)
            String feeSort,

            @RequestParam(required = false)
            String experienceSort
    ) {

        DoctorFilterRequest filter = new DoctorFilterRequest(
                search,
                gender,
                department,
                specialization,
                qualification,
                feeSort,
                experienceSort
        );

        return doctorService.getAllDoctorAdmin(
                page,
                size,
                filter
        );
    }

    @GetMapping("/get-allMedicines")
    public Page<MedicineAdminRes> getAllMedicines(

            @RequestParam(defaultValue = "0")
            Integer page,

            @RequestParam(defaultValue = "8")
            Integer size,

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            String category,

            @RequestParam(required = false)
            BatchStatus batchStatus,

            @RequestParam(required = false)
            String sortOption

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
