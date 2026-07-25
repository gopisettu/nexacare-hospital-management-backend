package com.nexacare.hospital.dto.response;

import java.util.Date;
import java.time.LocalDate;

public record TokenDto(
        String token,
        Date expiryTime
) {
}
