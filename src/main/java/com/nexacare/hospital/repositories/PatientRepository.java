package com.nexacare.hospital.repositories;

import com.nexacare.hospital.model.Patient;
import com.nexacare.hospital.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;



import java.util.Optional;

@Repository
public interface PatientRepository  extends JpaRepository<Patient,Long> {
    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByAadharNumber(String aadharNumber);

    Optional<Patient> findByUserId(Long id);

    Optional<Patient> findByUser(User user);

    @Query("""
SELECT p
FROM Patient p
WHERE p.user.isActive = true
""")
    Page<Patient> findAllExceptDeactivePatient(Pageable pageable);



    Page<Patient> findAll(Specification<Patient> spec, Pageable pageable);
}
