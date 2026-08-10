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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.http.HttpRequest;
import java.util.List;

@Configuration
@AllArgsConstructor
public class SpringSecurityConfig {
    private  final JwtFilter jwtFilter;
    private  final MyUserDetailService myUserDetailService;
    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth)->
                auth



                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/patient/register-patient").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/patient/loginPatient").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/patient/get-allPatient").permitAll()


                        .requestMatchers(HttpMethod.GET,"/api/patient/get-PatientByUsername/{username}").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/patient/update-patientProfile/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/patient/searchDoctor-bySpecialization/**").hasAuthority(Role.PATIENT.toString())
                        .requestMatchers(HttpMethod.GET, "/api/patient/searchDoctor-byDepartment/**").hasAuthority(Role.PATIENT.toString())
                        .requestMatchers(HttpMethod.POST,"/api/patient/book-doctorByPatient/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/patient/getAppointment-ByPatient/**").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/patient/view-PrescriptionByPatient/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/patient/payBill-ByPatient/**").hasAuthority(Role.PATIENT.toString())
                        .requestMatchers(HttpMethod.GET,"/api/patient/getPatientAppointments/{username}").permitAll()


                        .requestMatchers(HttpMethod.POST, "/api/doctor/register-doctor").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/doctor/loginDoctor").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/doctor/get-allDoctor").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/doctor/get-DoctorByUsername/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/doctor/update-doctorProfile/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/doctor/allAppointment-ByDoctor/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/doctor/rescheduleAppointment-ByDoctor/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/doctor/updateAppointmentStatus-ByDoctor/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/doctor/submitPrescription-byDoctor/**").permitAll()


                        .requestMatchers(HttpMethod.POST,"/api/doctor/submitPrescription-byDoctor/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/doctor/dashboard/{username}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/doctor/getListMedicine").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/admin/register-staffByAdmin").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST,"/api/admin/register-doctorByAdmin").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST,"/api/admin/loginAdmin").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/admin/addPatient-ByAdmin").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/admin/update-patientProfile/{username}").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/admin/image/upload/{patientId}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/admin/admin-dashboardAllRequiredData").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/admin/register-doctorByAdmin").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/admin/get-allDoctor").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/admin/doctorimage/upload/{doctorId}").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/admin/get-allMedicines").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/admin/addDoctor-ByAdmin").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/admin/updateDoctor-ByAdmin/{username}").permitAll()





                        .requestMatchers(HttpMethod.POST,"/api/executive/register-AdminByExecutive").permitAll()

                        .requestMatchers(HttpMethod.PUT,"api/executive/deActivatePatient-ByExecutive/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/executive/deActivateDoctor-ByExecutive/**").permitAll()


                        .requestMatchers(HttpMethod.POST,"/api/staff/loginStaff").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/staff/addMedicineStock-ByStaff/**").hasAuthority(Role.STAFF.toString())


                        .requestMatchers(HttpMethod.GET,"/api/enums/**").permitAll()
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        configuration.setAllowedMethods(
                List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
