package com.nexacare.hospital.dto.request.patientreq;

import com.nexacare.hospital.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PayBillDto(
        @NotNull(message="Payment Method is Required")
        PaymentMethod paymentMethod
) {
}