package com.nexacare.hospital.model;

import com.nexacare.hospital.enums.BloodGroup;
import com.nexacare.hospital.enums.Gender;
import com.nexacare.hospital.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "patient")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @Column(nullable = false)
    String full_name;
    @Enumerated(EnumType.STRING)
    Role role;
    @CreationTimestamp
    LocalDate createdAt;
}
