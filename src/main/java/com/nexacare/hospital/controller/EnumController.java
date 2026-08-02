package com.nexacare.hospital.controller;

import com.nexacare.hospital.enums.AppointmentStatus;
import com.nexacare.hospital.enums.BloodGroup;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.PaymentStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
        @RequestMapping("/api/enums")
@CrossOrigin(origins = "http://localhost:5173")
public class EnumController {
    @GetMapping("/genders")
    public Gender[] getGenders() {
        return Gender.values();
    }

    @GetMapping("/blood-groups")
    public BloodGroup[] getBloodGroups() {
        return BloodGroup.values();
    }

    @GetMapping("/appointment-status")
    public AppointmentStatus[] getAppointmentStatus() {
        return AppointmentStatus.values();
    }

    @GetMapping("/payment-status")
    public PaymentStatus[] getPaymentStatus() {
        return PaymentStatus.values();
    }
}
