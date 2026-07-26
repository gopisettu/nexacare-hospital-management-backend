package com.nexacare.hospital.repositories;

import com.nexacare.hospital.enums.BatchStatus;
import com.nexacare.hospital.model.MedicineBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineBatchRepository extends JpaRepository<MedicineBatch ,Long> {

    List<MedicineBatch> findByMedicineIdAndBatchStatusAndQuantityRemainingGreaterThanOrderByExpiryDateAsc(
            Long medicineId,
            BatchStatus batchStatus,
            Integer quantity
    );
}
