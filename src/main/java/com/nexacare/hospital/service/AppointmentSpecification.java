package com.nexacare.hospital.service;

import com.nexacare.hospital.enums.AppointmentStatus;
import com.nexacare.hospital.model.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AppointmentSpecification {

    // =========================================================
    // DOCTOR SPECIFICATION - EXISTING
    // =========================================================

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


    // =========================================================
    // PATIENT SPECIFICATION - NEW
    // =========================================================

    public static Specification<Appointment> patient(
            String username) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("patient")
                                .get("user")
                                .get("username"),
                        username
                );
    }


    // =========================================================
    // STATUS - EXISTING
    // =========================================================

    public static Specification<Appointment> status(
            AppointmentStatus status) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("appointmentStatus"),
                        status
                );
    }


    // =========================================================
    // FROM DATE - EXISTING
    // =========================================================

    public static Specification<Appointment> fromDate(
            LocalDate date) {

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("appointmentDate"),
                        date
                );
    }


    // =========================================================
    // TO DATE - EXISTING
    // =========================================================

    public static Specification<Appointment> toDate(
            LocalDate date) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("appointmentDate"),
                        date
                );
    }


    // =========================================================
    // TODAY
    // =========================================================

    public static Specification<Appointment> today() {

        LocalDate today = LocalDate.now();

        return (root, query, cb) ->
                cb.equal(
                        root.get("appointmentDate"),
                        today
                );
    }


    // =========================================================
    // UPCOMING
    // Future dates only
    // =========================================================

    public static Specification<Appointment> upcoming() {

        LocalDate today = LocalDate.now();

        return (root, query, cb) ->
                cb.greaterThan(
                        root.get("appointmentDate"),
                        today
                );
    }


    // =========================================================
    // PAST
    // Previous dates only
    // =========================================================

    public static Specification<Appointment> past() {

        LocalDate today = LocalDate.now();

        return (root, query, cb) ->
                cb.lessThan(
                        root.get("appointmentDate"),
                        today
                );
    }
}