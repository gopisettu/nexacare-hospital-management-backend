package com.nexacare.hospital.repositories;

import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Specialization;
import com.nexacare.hospital.model.Doctor;
import com.nexacare.hospital.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<Doctor> findByUserId(Long id);

    Optional<Doctor> findByUserUsername(String username);

@Query(
        """
           SELECT d  FROM Doctor d WHERE d.specialization=?1
                """
)
List<Doctor>searchDoctorBySpecialization(Specialization specialization);
    @Query(
            """
               SELECT d  FROM Doctor d WHERE d.department=?1
                    """
    )

    List<Doctor> searchDoctorByDepartment(Department department);
@Query("""
SELECT d
FROM Doctor d
WHERE d.user.isActive = true
""")

Page<Doctor> findAllActiveDoctors(Pageable pageable);

    Page<Doctor> findAll(Specification<Doctor> spec, Pageable pageable);
}
