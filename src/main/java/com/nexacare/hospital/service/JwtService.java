package com.nexacare.hospital.service;

import com.nexacare.hospital.dto.request.PatientRegisterDto;
import com.nexacare.hospital.dto.request.UserLoginDto;
import com.nexacare.hospital.dto.response.TokenDto;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {



    private static final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkey12";

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public TokenDto generateToken(PatientRegisterDto patientRegisterDto) {

        Map<String, Object> claims = new HashMap<>();
        Date expiryDate = new Date(System.currentTimeMillis() + 1000L * 60 * 30);
        String token=Jwts.builder()
                .claims(claims)
                .subject(patientRegisterDto.username())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(getKey())
                .compact();
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
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }
}
