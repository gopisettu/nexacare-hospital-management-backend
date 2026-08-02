package com.nexacare.hospital.controller;

import com.nexacare.hospital.dto.request.BookAppointmentDto;
import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.request.PatientProfileDto;
import com.nexacare.hospital.dto.request.PayBillDto;
import com.nexacare.hospital.dto.response.*;
import com.nexacare.hospital.enums.*;
import com.nexacare.hospital.service.AppointmentService;
import com.nexacare.hospital.service.DoctorService;
import com.nexacare.hospital.service.PatientService;
import com.nexacare.hospital.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor

@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;

    @PostMapping("/register-patient")
    public void registerPatient(@RequestBody LoginDto loginDto){
         patientService.registerPatient(loginDto);
    }
  @PostMapping("/loginPatient")
  public TokenDto loginPatient(@RequestBody
                               LoginDto loginDto){
        return patientService.loginPatient(loginDto);
  }


    @PutMapping("/update-patientProfile/{username}")
    public void updateProfile(@RequestBody PatientProfileDto patientProfileDto, @PathVariable String username){
        patientService.updateProfile(patientProfileDto,username);
    }
    @GetMapping("/get-allPatient")
    public List<PatientAdminResDto> getAllPatient(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "8") Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String appointmentFilter,
            @RequestParam(required = false) String sortOption) {

        return patientService.getAllPatient(page, size, search, gender, bloodGroup, appointmentFilter, sortOption);
    }
    @GetMapping("/get-PatientByUsername/{username}")
    public  PatientResDto getPatientByUsername(@PathVariable String username){
        return patientService.getPatientByUsername(username);
    }



    @PostMapping("/book-doctorByPatient/{username}")
    public void bookDoctor(@PathVariable String username, @RequestBody BookAppointmentDto bookAppointmentDto){
        appointmentService.bookDoctor(username, bookAppointmentDto);

    }
    @GetMapping("/getAppointment-ByPatient/{username}")
    public List<AppointmentResDto> showAllAppointmentByPatient(@PathVariable String username,
                                                              @RequestParam(required = false,defaultValue = "0") Integer page,
                                                              @RequestParam(required = false,defaultValue = "20") Integer size){
        return appointmentService.showAllAppointmentByPatient(username,page,size);

    }
    @GetMapping("/searchDoctor-bySpecialization/{username}/{specialization}")
    public List<DoctorResDto> searchDoctorBySpecialization(@PathVariable String username, @PathVariable Specialization specialization){
        return doctorService.searchDoctorBySpecialization(username,specialization);
    }

    @GetMapping("/searchDoctor-byDepartment/{username}/{department}")
    public List<DoctorResDto> searchDoctorByDepartment(@PathVariable String username, @PathVariable Department department){
        return doctorService.searchDoctorByDepartment(username,department);
    }
    @GetMapping("/view-PrescriptionByPatient/{username}")
    public List<PrescriptionResDto> viewPrescription(@PathVariable String username, @RequestParam Long appointmentId){
        return prescriptionService.viewPrescription(username,appointmentId);
    }
    @PatchMapping("/payBill-ByPatient/{appointmentId}")
    public void payBill(@PathVariable Long appointmentId,
                        @RequestParam String username,
                        @Valid @RequestBody PayBillDto dto) {
        appointmentService.payBill(username, appointmentId, dto);
    }



}
