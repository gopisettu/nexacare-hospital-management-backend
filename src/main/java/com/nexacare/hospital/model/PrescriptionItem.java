package com.nexacare.hospital.model;

import com.nexacare.hospital.enums.Frequency;
import com.nexacare.hospital.enums.Route;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prescription_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private String dosage;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private Integer durationDays;

    @Enumerated(EnumType.STRING)
    private Route route;

    private String instructions;

    @Column(nullable = false)
    private Integer quantity;
}
