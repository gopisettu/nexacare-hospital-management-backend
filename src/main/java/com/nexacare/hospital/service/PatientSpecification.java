package com.nexacare.hospital.service;

import com.nexacare.hospital.enums.AppointmentStatus;
import com.nexacare.hospital.model.Appointment;
import com.nexacare.hospital.model.Patient;
import com.nexacare.hospital.model.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientSpecification {
    private final static String APPOINTMENT_STATUS="appointmentStatus";

    private PatientSpecification(){

    }
    public static Specification<Patient> filterPatients(String search, String gender, String bloodGroup,
                                                        String appointmentFilter) {

        return (root, query, cb) -> {

            // Step 1: I will collect all my conditions in this list
            List<Predicate> predicates = new ArrayList<>();

            // Step 2: Only show patients whose user account is active
            Join<Patient, User> userJoin = root.join("user");
            Predicate activeCondition = cb.isTrue(userJoin.get("isActive"));
            predicates.add(activeCondition);

            // Step 3: If search text is given, filter by first name
            if (search != null && !search.isBlank()) {
                Predicate searchCondition = cb.like(cb.lower(root.get("firstName")), "%" + search.toLowerCase() + "%");
                predicates.add(searchCondition);
            }

            // Step 4: If gender is given, filter by gender
            if (gender != null && !gender.isBlank()) {
                Predicate genderCondition = cb.equal(root.get("gender"), gender);
                predicates.add(genderCondition);
            }

            // Step 5: If blood group is given, filter by blood group
            if (bloodGroup != null && !bloodGroup.isBlank()) {
                Predicate bloodGroupCondition = cb.equal(root.get("bloodGroup"), bloodGroup);
                predicates.add(bloodGroupCondition);
            }

            // Step 6: If appointment filter is given, filter by the patient's latest appointment
            if (appointmentFilter != null && !appointmentFilter.isBlank()) {
                Predicate appointmentCondition = buildAppointmentCondition(root, query, cb, appointmentFilter);
                predicates.add(appointmentCondition);
            }

            // Step 7: Combine all conditions with AND, and return the final query
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate buildAppointmentCondition(Root<Patient> root, CriteriaQuery<?> query,
                                                       CriteriaBuilder cb, String appointmentFilter) {

        // ----- PART A: Find the latest appointment date (createdAt) for this patient -----

        Subquery<Instant> latestDateSubquery = query.subquery(Instant.class);
        Root<Appointment> appointmentForDate = latestDateSubquery.from(Appointment.class);

        // Pick the maximum (latest) createdAt value
        latestDateSubquery.select(cb.greatest(appointmentForDate.<Instant>get("createdAt")));

        // Only look at appointments belonging to this same patient
        latestDateSubquery.where(cb.equal(appointmentForDate.get("patient"), root));

        // ----- PART B: Check if that latest appointment matches our filter -----

        Subquery<Long> matchingAppointment = query.subquery(Long.class);
        Root<Appointment> appointmentToCheck = matchingAppointment.from(Appointment.class);
        matchingAppointment.select(appointmentToCheck.get("id"));

        List<Predicate> appointmentConditions = new ArrayList<>();

        // Condition 1: must belong to this patient
        appointmentConditions.add(cb.equal(appointmentToCheck.get("patient"), root));

        // Condition 2: must be the latest appointment (matches the date we found in Part A)
        appointmentConditions.add(cb.equal(appointmentToCheck.get("createdAt"), latestDateSubquery));

        LocalDate today = LocalDate.now();

        // Condition 3: depends on which filter option was chosen
        if (appointmentFilter.equals("TODAY")) {

            appointmentConditions.add(cb.equal(appointmentToCheck.get("appointmentDate"), today));

        } else if (appointmentFilter.equals("UPCOMING")) {

            appointmentConditions.add(cb.greaterThan(appointmentToCheck.get("appointmentDate"), today));

            Predicate notCompleted = cb.notEqual(appointmentToCheck.get(APPOINTMENT_STATUS), AppointmentStatus.COMPLETED);
            Predicate notCancelled = cb.notEqual(appointmentToCheck.get(APPOINTMENT_STATUS), AppointmentStatus.CANCELLED);
            Predicate notNoShow = cb.notEqual(appointmentToCheck.get(APPOINTMENT_STATUS), AppointmentStatus.NO_SHOW);

            appointmentConditions.add(notCompleted);
            appointmentConditions.add(notCancelled);
            appointmentConditions.add(notNoShow);

        } else {

            // Direct status match, e.g. "COMPLETED", "SCHEDULED", "CANCELLED"
            AppointmentStatus statusToMatch = AppointmentStatus.valueOf(appointmentFilter);
            appointmentConditions.add(cb.equal(appointmentToCheck.get(APPOINTMENT_STATUS), statusToMatch));
        }

        matchingAppointment.where(appointmentConditions.toArray(new Predicate[0]));

        // Return true if such a matching appointment EXISTS for this patient
        return cb.exists(matchingAppointment);
    }
}