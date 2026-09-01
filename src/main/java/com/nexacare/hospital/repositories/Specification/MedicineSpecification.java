package com.nexacare.hospital.repositories.Specification;


import com.nexacare.hospital.enums.BatchStatus;
import com.nexacare.hospital.enums.MedicineCategory;
import com.nexacare.hospital.model.Medicine;
import com.nexacare.hospital.model.MedicineBatch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MedicineSpecification {

    private MedicineSpecification() {
    }

    public static Specification<Medicine> filterMedicines(
            String search,
            String category,
            BatchStatus batchStatus
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            /*
             * Medicine -> MedicineBatch
             *
             * LEFT JOIN is important.
             *
             * It allows medicines without a batch
             * to also appear in the result.
             */
            Join<Medicine, MedicineBatch> batch =
                    root.join("batches", JoinType.LEFT);

            // Search by medicine name
            if (search != null && !search.isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + search.toLowerCase() + "%"
                        )
                );
            }

            // Category filter
            if (category != null && !category.isBlank()) {

                try {

                    MedicineCategory medicineCategory =
                            MedicineCategory.valueOf(
                                    category.toUpperCase()
                            );

                    predicates.add(
                            cb.equal(
                                    root.get("category"),
                                    medicineCategory
                            )
                    );

                } catch (IllegalArgumentException e) {

                    return cb.disjunction();
                }
            }

            // Batch status filter
            if (batchStatus != null) {

                predicates.add(
                        cb.equal(
                                batch.get("batchStatus"),
                                batchStatus
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }

    public static Sort getSort(String sortOption) {

        if (sortOption == null || sortOption.isBlank()) {
            return Sort.unsorted();
        }

        return switch (sortOption.toUpperCase()) {

            case "EXPIRY_ASC" ->
                    Sort.by("batches.expiryDate").ascending();

            case "EXPIRY_DESC" ->
                    Sort.by("batches.expiryDate").descending();

            case "STOCK_LOW" ->
                    Sort.by("batches.quantityRemaining").ascending();

            case "STOCK_HIGH" ->
                    Sort.by("batches.quantityRemaining").descending();

            case "PRICE_LOW" ->
                    Sort.by("unitPrice").ascending();

            case "PRICE_HIGH" ->
                    Sort.by("unitPrice").descending();

            default ->
                    Sort.unsorted();
        };
    }
}