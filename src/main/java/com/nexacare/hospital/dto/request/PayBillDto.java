package com.nexacare.hospital.dto.request;

import com.nexacare.hospital.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PayBillDto(
        @NotNull
        PaymentMethod paymentMethod
) {
}