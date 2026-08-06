package com.nexacare.hospital.dto.response.AuthRes;

import java.util.Date;


public record TokenDto(
        String token,
        Date expiryTime
) {
}
