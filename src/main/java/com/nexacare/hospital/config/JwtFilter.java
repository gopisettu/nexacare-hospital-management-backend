package com.nexacare.hospital.config;

import com.nexacare.hospital.service.JwtService;
import com.nexacare.hospital.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MyUserDetailService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("==== JWT FILTER EXECUTED ====");

        String authHeader = request.getHeader("Authorization");

        System.out.println("Authorization Header = " + authHeader);

        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            System.out.println("Token = " + token);

            username = jwtService.extractUsername(token);

            System.out.println("Username From Token = " + username);
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    myUserDetailsService.loadUserByUsername(username);

            System.out.println("Username = " + userDetails.getUsername());
            System.out.println("Authorities = " + userDetails.getAuthorities());

            System.out.println("User Loaded = " + userDetails.getUsername());

            if (jwtService.validateToken(token, userDetails)) {

                System.out.println("TOKEN VALID");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println(
                        "Authenticated Authorities = " +
                                SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                );
            } else {
                System.out.println("TOKEN INVALID");
            }
        }

        filterChain.doFilter(request, response);
    }

}