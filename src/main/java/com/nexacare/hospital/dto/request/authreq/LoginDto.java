package com.nexacare.hospital.dto.request.authreq;

import jakarta.validation.constraints.NotBlank;

public record  LoginDto(
        @NotBlank(message = "Username is mandatory")
        String username,
        @NotBlank(message = "Password is mandatory")
        String password)
{
}
