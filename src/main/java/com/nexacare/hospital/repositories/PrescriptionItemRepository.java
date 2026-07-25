package com.nexacare.hospital.repositories;

import com.nexacare.hospital.model.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem ,Long> {

    boolean existsByAppointmentId(Long id);

    List<PrescriptionItem> findPrescriptionByAppointmentId(Long appointmentId);
}
