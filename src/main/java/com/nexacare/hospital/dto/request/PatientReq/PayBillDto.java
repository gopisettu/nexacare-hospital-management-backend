package com.nexacare.hospital.dto.request.PatientReq;

import com.nexacare.hospital.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PayBillDto(
        @NotNull(message="Payment Method is Required")
        PaymentMethod paymentMethod
) {
}