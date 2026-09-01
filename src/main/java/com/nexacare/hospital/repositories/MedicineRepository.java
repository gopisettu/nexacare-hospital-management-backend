package com.nexacare.hospital.repositories;

import com.nexacare.hospital.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
public interface MedicineRepository
        extends JpaRepository<Medicine, Long>,
        JpaSpecificationExecutor<Medicine> {
    List<Medicine> findTop5ByOrderByCreatedAtDesc();
}