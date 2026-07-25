package com.nexacare.hospital.config;

import com.nexacare.hospital.service.JwtService;
import com.nexacare.hospital.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MyUserDetailService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info("JWT Filter executed.");

        String authHeader = request.getHeader("Authorization");

        log.debug("Authorization Header: {}", authHeader);

        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);
            log.debug("JWT token received.");

            username = jwtService.extractUsername(token);

            log.debug("Username extracted from token: {}", username);
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    myUserDetailsService.loadUserByUsername(username);

            log.debug("Loaded user: {}", userDetails.getUsername());
            log.debug("User authorities: {}", userDetails.getAuthorities());

            if (jwtService.validateToken(token, userDetails)) {

                log.info("JWT token validated successfully for user '{}'.", username);

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

                log.debug("Authenticated authorities: {}",
                        SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getAuthorities());
            } else {
                log.warn("JWT validation failed for user '{}'.", username);
            }
        }

        filterChain.doFilter(request, response);
    }

}