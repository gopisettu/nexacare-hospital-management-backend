package com.nexacare.hospital.dto.response.AuthRes;

import jakarta.validation.constraints.NotNull;

import java.util.Date;



public record TokenDto(
        String token,
        Date expiryTime,
        String username,
        String role
) { }