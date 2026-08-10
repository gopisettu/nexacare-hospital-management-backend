package com.nexacare.hospital.dto.response.authres;

import com.nexacare.hospital.enums.Role;

public record LoginResDto(
        Long userId,
        String username,
        Role role
) {

}
