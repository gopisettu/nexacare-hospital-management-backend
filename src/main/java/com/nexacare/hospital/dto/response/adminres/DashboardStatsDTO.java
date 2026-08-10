package com.nexacare.hospital.dto.response.adminres;

public record DashboardStatsDTO(
        Long totalPatients,
        Long totalDoctors,
        Long totalAppointments
)  {


}