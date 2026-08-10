package com.nexacare.hospital.dto.response.authres;

import java.util.Date;



public record TokenDto(
        String token,
        Date expiryTime,
        String username,
        String role
) { }