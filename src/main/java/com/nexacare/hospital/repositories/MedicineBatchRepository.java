package com.nexacare.hospital.repositories;

import com.nexacare.hospital.enums.BatchStatus;
import com.nexacare.hospital.model.MedicineBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicineBatchRepository extends JpaRepository<MedicineBatch ,Long> {

    @Query(value = """
            SELECT *
            FROM medicine_batch
            WHERE medicine_id = :medicineId
              AND batch_status = 'ACTIVE'
              AND quantity_remaining > 0
            ORDER BY expiry_date ASC
            """, nativeQuery = true)
    List<MedicineBatch> findAvailableBatches(Long medicineId);
    Page<MedicineBatch> findAll(
            Specification<MedicineBatch> spec,
            Pageable pageable
    );
}
