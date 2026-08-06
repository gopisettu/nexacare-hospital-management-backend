package com.nexacare.hospital.dto.response.AuthRes;

import com.nexacare.hospital.enums.Role;

public record LoginResDto(
        Long userId,
        String username,
        Role role
) {

}
