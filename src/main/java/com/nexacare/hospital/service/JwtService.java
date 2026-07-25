package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.response.TokenDto;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;



    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public TokenDto generateToken(String username) {
        log.info("Generating JWT token for user '{}'.", username);

        Map<String, Object> claims = new HashMap<>();
        Date expiryDate = new Date(System.currentTimeMillis() + 1000L* 60 * 60*60);
        String token=Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(getKey())
                .compact();
        log.info("JWT token generated successfully for user '{}'.", username);
        return new TokenDto(
                token,
                expiryDate
        );

    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        boolean valid =
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token);

        if (valid) {
            log.info("JWT validated successfully for user '{}'.", username);
        } else {
            log.warn("JWT validation failed for user '{}'.", username);
        }
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }
}
