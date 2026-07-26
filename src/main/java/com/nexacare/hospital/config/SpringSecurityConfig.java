package com.nexacare.hospital.config;

import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.service.MyUserDetailService;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.patterns.HasMemberTypePatternForPerThisMatching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.http.HttpRequest;

@Configuration
@AllArgsConstructor
public class SpringSecurityConfig {
    private  final JwtFilter jwtFilter;
    private  final MyUserDetailService myUserDetailService;
    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf((c)->c.disable())
                .authorizeHttpRequests((auth)->
                auth  .requestMatchers(HttpMethod.POST, "/api/patient/register-patient").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/patient/loginPatient").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/patient/get-allPatient").hasAnyAuthority(Role.DOCTOR.toString(),Role.STAFF.toString())
                        .requestMatchers(HttpMethod.GET, "/api/patient/get-ByUserName/**").hasAnyAuthority(Role.DOCTOR.toString(),Role.STAFF.toString())
                        .requestMatchers(HttpMethod.PUT, "/api/patient/update-patientProfile/**").hasAuthority(Role.PATIENT.toString())

                        .requestMatchers(HttpMethod.GET, "/api/patient/searchDoctor-bySpecialization/**").hasAuthority(Role.PATIENT.toString())
                        .requestMatchers(HttpMethod.GET, "/api/patient/searchDoctor-byDepartment/**").hasAuthority(Role.PATIENT.toString())
                        .requestMatchers(HttpMethod.POST, "/api/patient/book-doctorByPatient/**").hasAuthority(Role.PATIENT.toString())
                        .requestMatchers(HttpMethod.GET, "/api/patient/getAppointment-ByPatient/**").hasAuthority(Role.PATIENT.toString())

                        .requestMatchers(HttpMethod.GET,"/api/patient/view-PrescriptionByPatient/**").hasAuthority(Role.PATIENT.toString())



                        .requestMatchers(HttpMethod.POST, "/api/doctor/register-doctor").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/doctor/loginDoctor").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/doctor/get-allDoctor").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/doctor/get-ByUsername/**").hasAuthority(Role.DOCTOR.toString())
                        .requestMatchers(HttpMethod.PUT, "/api/doctor/update-doctorProfile/**").hasAuthority(Role.DOCTOR.toString())

                        .requestMatchers(HttpMethod.GET, "/api/doctor/allAppointment-ByDoctor/**").hasAuthority(Role.DOCTOR.toString())
                        .requestMatchers(HttpMethod.PATCH, "/api/doctor/rescheduleAppointment-ByDoctor/**").hasAuthority(Role.DOCTOR.toString())
                        .requestMatchers(HttpMethod.PATCH, "/api/doctor/updateAppointmentStatus-ByDoctor/**").hasAuthority(Role.DOCTOR.toString())
                        .requestMatchers(HttpMethod.POST, "/api/doctor/submitPrescription-byDoctor/**").hasAuthority(Role.DOCTOR.toString())

                        .requestMatchers(HttpMethod.POST,"/api/admin/register-staffByAdmin").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST,"/api/admin/register-doctorByAdmin").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST,"/api/admin/loginAdmin").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/executive/register-AdminByExecutive").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/executive/deActivatePatient-ByExecutive/**").hasAuthority(Role.EXECUTIVE.toString())
                        .requestMatchers(HttpMethod.PUT, "/api/executive/deActivateDoctor-ByExecutive/**").hasAuthority(Role.EXECUTIVE.toString())


                        .requestMatchers(HttpMethod.POST,"/api/staff/loginStaff").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/staff/addMedicineStock-ByStaff/**").hasAuthority(Role.STAFF.toString())
                );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
         http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
     public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(myUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(myUserDetailService);
        return daoAuthenticationProvider;
    }
}
