package com.nexacare.hospital.repositories;

import com.nexacare.hospital.dto.response.DoctorRes.AppointmentResDto;
import com.nexacare.hospital.model.Appointment;
import com.nexacare.hospital.model.MedicineBatch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
            SELECT a
            FROM Appointment a
            WHERE a.doctor.user.username = ?1
            ORDER BY a.appointmentDate DESC
            """)
    List<Appointment> findByDoctorUserUsername(String username, Pageable pageable);

    @Query("""
            SELECT a
            FROM Appointment a
            WHERE a.patient.user.username = ?1
            ORDER BY a.appointmentDate DESC
            """)
    List<Appointment> findByPatientUserUsername(String username, Pageable pageable);

    @Query(value = """
    SELECT COUNT(*)
    FROM appointment
    WHERE doctor_id = :doctorId
      AND appointment_date = :appointmentDate
      AND appointment_time = :appointmentTime
    """, nativeQuery = true)
    long countConflictingAppointments(
            @Param("doctorId") Long doctorId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("appointmentTime") LocalTime appointmentTime);

    Appointment findTopByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
    interface MedicineBatchRepository extends JpaRepository<MedicineBatch,Long> {

    }
}