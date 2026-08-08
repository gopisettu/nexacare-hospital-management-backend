package com.nexacare.hospital.dto.request.ImageUploadReq;

public record
UploadDto(

        long patientId,
        String path,
        String fileName,
        String message
) {
}
