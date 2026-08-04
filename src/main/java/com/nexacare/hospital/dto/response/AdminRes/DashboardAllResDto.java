package com.nexacare.hospital.dto.response.AdminRes;

import com.nexacare.hospital.dto.response.DoctorRes.AppointmentResDto;

import java.util.List;
import java.util.Map;

public record DashboardAllResDto
        (

                DashboardStatsDTO stats,

                Map<String, Long> reasonDistribution,

                List<AppointmentResDto> todayAppointments,

                List<MedicineResDto> medicines

        ) {
}
