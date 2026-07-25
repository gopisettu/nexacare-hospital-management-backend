package com.nexacare.hospital.repositories;

import com.nexacare.hospital.model.Staff;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository  extends JpaRepository<Staff,Long> {
}
