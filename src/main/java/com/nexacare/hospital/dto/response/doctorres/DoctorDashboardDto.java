package com.nexacare.hospital.dto.response.doctorres;


import java.util.List;
import java.util.Map;

public record DoctorDashboardDto(

        DoctorResDto doctor,

        Long totalPatients,
        Long totalAppointments,
        Long todayAppointments,
        Long completedAppointments,

        Map<String, Long> reasonDistribution,

        List<AppointmentResDto> todayAppointmentList,

        List<AppointmentResDto> upcomingAppointments

) {
}