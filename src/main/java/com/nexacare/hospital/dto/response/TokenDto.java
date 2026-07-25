package com.nexacare.hospital.dto.response;

import java.util.Date;


public record TokenDto(
        String token,
        Date expiryTime
) {
}
