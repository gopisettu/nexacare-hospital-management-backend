package com.nexacare.hospital.service;

import com.nexacare.hospital.exception.ResourceNotFoundException;
import com.nexacare.hospital.model.Medicine;
import com.nexacare.hospital.repositories.MedicineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public Medicine validateMedicine(Long medicineId) {

        return medicineRepository.findById(medicineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine not found : " + medicineId));
    }
}