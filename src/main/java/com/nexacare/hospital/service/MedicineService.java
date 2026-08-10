package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.response.adminres.MedicineAdminRes;
import com.nexacare.hospital.enums.BatchStatus;
import com.nexacare.hospital.exception.ResourceNotFoundException;
import com.nexacare.hospital.mapper.dtotoentity.MedicineMapper;
import com.nexacare.hospital.model.Medicine;
import com.nexacare.hospital.model.MedicineBatch;
import com.nexacare.hospital.repositories.MedicineBatchRepository;
import com.nexacare.hospital.repositories.MedicineRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineBatchRepository medicineBatchRepository;
    private final MedicineMapper medicineMapper;

    public Medicine validateMedicine(Long medicineId) {

        return medicineRepository.findById(medicineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicine not found : " + medicineId));
    }


    public Page<MedicineAdminRes> getAllMedicines(
            Integer page,
            Integer size,
            String search,
            String category,
            BatchStatus batchStatus,
            String sortOption) {

        Specification<MedicineBatch> spec =
                MedicineSpecification.filterMedicines(
                        search,
                        category,
                        batchStatus
                );

        Pageable pageable = PageRequest.of(
                page,
                size,
                MedicineSpecification.getSort(sortOption)
        );

        Page<MedicineBatch> medicinePage =
                medicineBatchRepository.findAll(spec, pageable);

        return medicinePage.map(medicineMapper::mapMedicineAdminRes);
    }

    public List<Medicine> getMedicineList() {

        return medicineRepository.findAll();
    }
}