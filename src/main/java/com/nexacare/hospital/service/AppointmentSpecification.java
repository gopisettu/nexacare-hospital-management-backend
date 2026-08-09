package com.nexacare.hospital.service;

import com.nexacare.hospital.model.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import com.nexacare.hospital.enums.AppointmentStatus;
public class AppointmentSpecification {

    public static Specification<Appointment> doctor(
            String username) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("doctor")
                                .get("user")
                                .get("username"),
                        username
                );
    }

    public static Specification<Appointment> status(
            AppointmentStatus status) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("appointmentStatus"),
                        status
                );
    }

    public static Specification<Appointment> fromDate(
            LocalDate date) {

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("appointmentDate"),
                        date
                );
    }

    public static Specification<Appointment> toDate(
            LocalDate date) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("appointmentDate"),
                        date
                );
    }
}