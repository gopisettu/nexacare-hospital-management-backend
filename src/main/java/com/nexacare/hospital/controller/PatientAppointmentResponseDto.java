package com.nexacare.hospital.controller;


import com.nexacare.hospital.dto.response.doctorres.AppointmentResDto;

import java.util.List;

public record PatientAppointmentResponseDto(

        // Today's appointments
        List<AppointmentResDto> todayAppointments,

        // Upcoming appointments + pagination
        List<AppointmentResDto> upcomingAppointments,
        long upcomingTotalElements,
        int upcomingTotalPages,
        int upcomingCurrentPage,

        // Past appointments + pagination
        List<AppointmentResDto> pastAppointments,
        long pastTotalElements,
        int pastTotalPages,
        int pastCurrentPage

) {
}