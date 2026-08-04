package com.nexacare.hospital.model;

import com.nexacare.hospital.enums.Department;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.Qualification;
import com.nexacare.hospital.enums.Specialization;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Everything below is filled in later via PUT /update-profile,
    // so nothing here can be NOT NULL at registration time.

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true, length = 10)
    private String phone;

    @Column(unique = true)
    private String email;

    @Column(length = 255)
    private String address;


    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private Integer totalExperienceYear;

    private Double consultationFee;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;
    private String imageUrl;
}