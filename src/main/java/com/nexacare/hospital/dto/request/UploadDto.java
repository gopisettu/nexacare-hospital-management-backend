package com.nexacare.hospital.dto.request;

public record
UploadDto(

        long patientId,
        String path,
        String fileName,
        String message
) {
}
