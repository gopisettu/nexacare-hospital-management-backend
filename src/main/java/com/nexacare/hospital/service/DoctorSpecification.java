package com.nexacare.hospital.service;

import com.nexacare.hospital.model.Doctor;
import com.nexacare.hospital.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DoctorSpecification {
private  DoctorSpecification(){

}
    public static Specification<Doctor> filterDoctors(

            String search,
            String gender,
            String department,
            String specialization,
            String qualification
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Active Users Only

            Join<Doctor, User> userJoin = root.join("user");

            predicates.add(cb.isTrue(userJoin.get("isActive")));

            // Search

            if (search != null && !search.isBlank()) {

                Predicate firstName =
                        cb.like(
                                cb.lower(root.get("firstName")),
                                "%" + search.toLowerCase() + "%"
                        );

                Predicate lastName =
                        cb.like(
                                cb.lower(root.get("lastName")),
                                "%" + search.toLowerCase() + "%"
                        );

                Predicate email =
                        cb.like(
                                cb.lower(userJoin.get("username")),
                                "%" + search.toLowerCase() + "%"
                        );

                predicates.add(
                        cb.or(firstName, lastName, email)
                );
            }

            // Gender

            if (gender != null && !gender.isBlank()) {

                predicates.add(
                        cb.equal(root.get("gender"), gender)
                );
            }

            // Department

            if (department != null && !department.isBlank()) {

                predicates.add(
                        cb.equal(root.get("department"), department)
                );
            }

            // Specialization

            if (specialization != null && !specialization.isBlank()) {

                predicates.add(
                        cb.equal(root.get("specialization"), specialization)
                );
            }

            // Qualification

            if (qualification != null && !qualification.isBlank()) {

                predicates.add(
                        cb.equal(root.get("qualification"), qualification)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}