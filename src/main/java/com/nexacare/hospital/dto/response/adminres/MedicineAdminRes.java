package com.nexacare.hospital.dto.response.adminres;



import com.nexacare.hospital.enums.BatchStatus;
import com.nexacare.hospital.enums.MedicineForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineAdminRes {

    private Long id;

    // Medicine Details
    private String name;

    private String dosage;

    private String manufacturer;

    private String category;

    private MedicineForm medicineForm;

    private Double unitPrice;

    // Batch Details
    private String batchNo;

    private Integer quantityReceived;

    private Integer quantityRemaining;

    private LocalDate expiryDate;

    private BatchStatus batchStatus;
}