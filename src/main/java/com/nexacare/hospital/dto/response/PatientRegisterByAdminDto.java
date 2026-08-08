package com.nexacare.hospital.dto.response;


import java.time.LocalDate;


import com.nexacare.hospital.enums.BloodGroup;
import com.nexacare.hospital.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PatientRegisterByAdminDto {

    // User Credentials
    @NotBlank(message = "Username is required")
    @Email(message = "Username must be a valid email")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // Patient Details
    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of Birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Aadhar Number is required")
    @Pattern(regexp = "\\d{12}", message = "Aadhaar must be 12 digits")
    private String aadharNumber;

    @NotNull(message = "Blood Group is required")
    private BloodGroup bloodGroup;

    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone Number must be 10 digits")
    private String phone;

    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    private String allergies;

    private String chronicDisease;
}