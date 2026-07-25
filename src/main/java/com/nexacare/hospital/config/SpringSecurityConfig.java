package com.nexacare.hospital.config;

import com.nexacare.hospital.enums.Role;
import com.nexacare.hospital.service.MyUserDetailService;
import lombok.AllArgsConstructor;
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
                auth    .requestMatchers(HttpMethod.POST,"/api/patient/register-patient").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/patient/get-allPatient").hasAuthority(Role.PATIENT.toString())
                        .requestMatchers(HttpMethod.GET,"/api/doctor/get-allDoctor").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/patient/loginPatient").permitAll()

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
