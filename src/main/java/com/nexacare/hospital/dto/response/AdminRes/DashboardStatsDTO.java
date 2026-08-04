package com.nexacare.hospital.dto.response.AdminRes;

public record DashboardStatsDTO(
        Long totalPatients,
        Long totalDoctors,
        Long totalAppointments
)  {


}