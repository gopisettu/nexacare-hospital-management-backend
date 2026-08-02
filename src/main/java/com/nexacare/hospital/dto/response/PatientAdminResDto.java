package com.nexacare.hospital.dto.response;

import com.nexacare.hospital.enums.AppointmentStatus;
import com.nexacare.hospital.enums.BloodGroup;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record PatientAdminResDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        Gender gender,
        LocalDate dob,
        String aadharNumber,
        BloodGroup bloodGroup,
        String phone,
        String email,
        String address,
        String allergies,
        String chronicDisease,
        PaymentStatus paymentStatus,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        AppointmentStatus appointmentStatus

) {

}
