package com.nexacare.hospital.repositories;

import com.nexacare.hospital.model.Appointment;

import com.nexacare.hospital.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);
    @Query("""
            Select u
                from User u
                    where u.username=?1
            """)
    Optional<User> findByUserUsername(String username);

@Query("""
SELECT a FROM Appointment a WHERE a.doctor.id=?1

""")
List<Appointment> getAllAppointmentByDoctor(Long id, Pageable pageable);

    Optional<User> findByUsername(String username);
}
