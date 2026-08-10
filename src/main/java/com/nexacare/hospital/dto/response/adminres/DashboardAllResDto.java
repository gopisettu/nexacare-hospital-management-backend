package com.nexacare.hospital.dto.response.adminres;

import com.nexacare.hospital.dto.response.doctorres.AppointmentResDto;

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
