package com.nexacare.hospital.controller;

import com.nexacare.hospital.dto.request.AuthReq.LoginDto;
import com.nexacare.hospital.dto.request.DoctorReq.DoctorProfileDto;
import com.nexacare.hospital.dto.request.DoctorReq.RescheduleAppointmentDto;
import com.nexacare.hospital.dto.request.DoctorReq.SubmitPrescriptionDto;
import com.nexacare.hospital.dto.request.DoctorReq.UpdateAppointmentStatusDto;
import com.nexacare.hospital.dto.response.DoctorRes.AppointmentResDto;
import com.nexacare.hospital.dto.response.DoctorRes.DoctorDashboardDto;
import com.nexacare.hospital.dto.response.DoctorRes.DoctorResDto;
import com.nexacare.hospital.dto.response.AuthRes.TokenDto;
import com.nexacare.hospital.enums.AppointmentPeriod;
import com.nexacare.hospital.enums.AppointmentStatus;
import com.nexacare.hospital.service.AppointmentService;
import com.nexacare.hospital.service.DoctorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class DoctorController {
    private final DoctorService doctorService;
    private  final AppointmentService appointmentService;

    @PostMapping("/loginDoctor")
    public TokenDto loginDoctor( @Valid @RequestBody LoginDto loginDto ){
        return doctorService.loginDoctor(loginDto);
    }

    @PutMapping("/update-doctorProfile/{username}")
    public void updateProfile(@Valid @RequestBody DoctorProfileDto doctorProfileDto, @PathVariable String username){
        doctorService.updateProfile(doctorProfileDto,username);
    }
    @GetMapping("/get-allDoctor")
    public List<DoctorResDto> getAllDoctor( @RequestParam(required = false,defaultValue = "0") Integer page,
                                           @RequestParam(required = true,defaultValue = "4") Integer size){
        return doctorService.getAllDoctor(page,size);
    }

    @GetMapping("/get-DoctorByUsername/{username}")
    public DoctorResDto getDoctorByUsername( @PathVariable String username){
        return doctorService.getDoctorByUsername(username);
    }
    @GetMapping("/allAppointment-ByDoctor/{username}")
    public Page<AppointmentResDto> showAllAppointmentByDoctor(

            @PathVariable String username,

            @RequestParam(required = false, defaultValue = "TODAY")
            AppointmentPeriod period,

            @RequestParam(required = false)
            AppointmentStatus status,

            @RequestParam(required = false, defaultValue = "0")
            Integer page,

            @RequestParam(required = false, defaultValue = "10")
            Integer size) {

        return appointmentService.showAllAppointmentByDoctor(
                username,
                period,
                status,
                page,
                size
        );
    }
    @PatchMapping("/updateAppointmentStatus-ByDoctor/{username}")
    public void updateAppointmentStatus(@PathVariable String username, @Valid @RequestBody UpdateAppointmentStatusDto updateAppointmentStatusDto){
        appointmentService.updateAppointmentStatus(username,updateAppointmentStatusDto);

    }
    @PatchMapping("/rescheduleAppointment-ByDoctor/{username}")
    public  void rescheduleAppointment(@PathVariable String username, @Valid @RequestBody RescheduleAppointmentDto rescheduleAppointmentDto){
        appointmentService.rescheduleAppointment(username,rescheduleAppointmentDto);
    }
    @PostMapping("/submitPrescription-byDoctor/{username}")
    public void submitPrescription(@PathVariable String username, @Valid @RequestBody SubmitPrescriptionDto submitPrescriptionDto)
    {
        appointmentService.submitPrescription(username,submitPrescriptionDto);
    }

    @GetMapping("/dashboard/{username}")
    public DoctorDashboardDto getDashboard(
            @PathVariable String username){

        return doctorService.getDoctorDashboard(username);
    }
}

