package com.nexacare.hospital.model;

import com.nexacare.hospital.enums.BloodGroup;
import com.nexacare.hospital.enums.Gender;
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
public class Patient {

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
     private String lastName;


     @Enumerated(EnumType.STRING)
     private Gender gender;

     private LocalDate dob;

     @Column(unique = true, length = 12)
     private String aadharNumber;

     @Enumerated(EnumType.STRING)
     private BloodGroup bloodGroup;

     private String phone;

     @Column(unique = true)
     private String email;

     private String address;
     private String allergies;
     private String chronicDisease;

     @CreationTimestamp
     private Instant createdAt;
     private String imageUrl;


}