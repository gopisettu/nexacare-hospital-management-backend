package com.nexacare.hospital.dto.request.AdminReq;
import java.time.LocalDate;
import com.nexacare.hospital.enums.BloodGroup;
import com.nexacare.hospital.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    private String firstName;


    private String lastName;


    private Gender gender;


    private LocalDate dob;


    private String aadharNumber;


    private BloodGroup bloodGroup;


    private String phone;


    private String email;


    private String address;

    private String allergies;

    private String chronicDisease;
}