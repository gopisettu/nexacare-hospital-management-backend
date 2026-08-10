package com.nexacare.hospital.repositories.Specification;


import com.nexacare.hospital.enums.BatchStatus;
import com.nexacare.hospital.model.Medicine;
import com.nexacare.hospital.model.MedicineBatch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MedicineSpecification {

    private MedicineSpecification() {
    }

    public static Specification<MedicineBatch> filterMedicines(
            String search,
            String category,
            BatchStatus batchStatus
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            Join<MedicineBatch, Medicine> medicine = root.join("medicine");

            // Search by Medicine Name
            if (search != null && !search.isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(medicine.get("name")),
                                "%" + search.toLowerCase() + "%"
                        )
                );
            }

            // Category Filter
            if (category != null && !category.isBlank()) {

                predicates.add(
                        cb.equal(
                                medicine.get("category"),
                                category
                        )
                );
            }

            // Batch Status Filter
            if (batchStatus != null) {

                predicates.add(
                        cb.equal(
                                root.get("batchStatus"),
                                batchStatus
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Sort getSort(String sortOption) {

        if (sortOption == null || sortOption.isBlank()) {
            return Sort.unsorted();
        }

        return switch (sortOption) {

            case "EXPIRY_ASC" ->
                    Sort.by("expiryDate").ascending();

            case "EXPIRY_DESC" ->
                    Sort.by("expiryDate").descending();

            case "STOCK_LOW" ->
                    Sort.by("quantityRemaining").ascending();

            case "STOCK_HIGH" ->
                    Sort.by("quantityRemaining").descending();

            case "PRICE_LOW" ->
                    Sort.by("medicine.unitPrice").ascending();

            case "PRICE_HIGH" ->
                    Sort.by("medicine.unitPrice").descending();

            default ->
                    Sort.unsorted();
        };
    }

}