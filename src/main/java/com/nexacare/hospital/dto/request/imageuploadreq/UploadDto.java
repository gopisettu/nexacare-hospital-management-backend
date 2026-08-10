package com.nexacare.hospital.dto.request.imageuploadreq;

public record
UploadDto(

        long patientId,
        String path,
        String fileName,
        String message
) {
}
