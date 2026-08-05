package com.nexacare.hospital.model;

import com.nexacare.hospital.enums.MedicineCategory;
import com.nexacare.hospital.enums.MedicineForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "medicine")
public class Medicine {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String dosage;

    private String manufacturer;

    @Enumerated(EnumType.STRING)
    private MedicineCategory category;

    @Enumerated(EnumType.STRING)
    private MedicineForm medicineForm;

    private Double unitPrice;

    @CreationTimestamp
    private Instant createdAt;
}
