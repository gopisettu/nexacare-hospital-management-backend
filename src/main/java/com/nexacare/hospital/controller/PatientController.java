package com.nexacare.hospital.controller;

import com.nexacare.hospital.dto.request.BookAppointmentDto;
import com.nexacare.hospital.dto.request.LoginDto;
import com.nexacare.hospital.dto.request.PatientProfileDto;
import com.nexacare.hospital.dto.request.PatientRegisterDto;
import com.nexacare.hospital.dto.response.AppointmentResDto;
import com.nexacare.hospital.dto.response.DoctorResDto;
import com.nexacare.hospital.dto.response.PatientResDto;
import com.nexacare.hospital.dto.response.TokenDto;
import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Specialization;
import com.nexacare.hospital.service.AppointmentService;
import com.nexacare.hospital.service.DoctorService;
import com.nexacare.hospital.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
/*
{
    "username":"raja@gmail.com",
    "password":"raja@123"
}
 */
    @PostMapping("/register-patient")
    public void registerPatient(@RequestBody PatientRegisterDto patientDto){
         patientService.registerPatient(patientDto);
    }
  @PostMapping("/loginPatient")
  public TokenDto loginPatient(@RequestBody
                               LoginDto loginDto){
        return patientService.loginPatient(loginDto);
  }


/*
{
  "firstName": "Arun",
  "lastName": "Kumar",
  "gender": "MALE",
  "phone": "9876543210",
  "email": "arun.kumar@nexacare.com",
  "address": "No. 45, Anna Nagar, Chennai, Tamil Nadu",
  "qualification": "MBBS",
  "department": "CARDIOLOGY",
  "specialization": "CARDIOLOGIST",
  "totalExperienceYear": 12,
  "consultationFee": 750.0
}
 */
    @PutMapping("/update-patientProfile/{username}")
    public void updateProfile(@RequestBody PatientProfileDto patientProfileDto, @PathVariable String username){
        patientService.updateProfile(patientProfileDto,username);
    }

    @GetMapping("/get-allPatient")
    public List<PatientResDto> getAllPatient(
            @RequestParam(required = false,defaultValue = "0") Integer page,
            @RequestParam(required = false,defaultValue = "4") Integer  size){
         return patientService.getAllPatient(page,size);
    }
    @GetMapping("/get-PatientByUsername/{username}")
    public  PatientResDto getPatientByUsername(@PathVariable String username){
        return patientService.getPatientByUsername(username);
    }

    @PutMapping("/deActivate-ByPatient/{username}")
    public void deActivatePatient(@PathVariable String username){
        patientService.deActivatePatient(username);
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
//    @GetMapping("/view-PrescriptionByPatient/{username}")
//    public PrescriptionResDto viewPrescription(@PathVariable String username,@RequestParam Long appointmentId,)


}
