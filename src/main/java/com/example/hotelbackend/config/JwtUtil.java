package com.example.hotelbackend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Must be 32+ chars
    private static final String SECRET =
            "hotel-backend-super-secret-key-12345";

    private static final long EXPIRATION =
            1000 * 60 * 60 * 24; // 24 hours

    private final Key key = Keys.hmacShaKeyFor(
            SECRET.getBytes(StandardCharsets.UTF_8)
    );


    /* =====================================================
       GENERATE TOKEN
       ===================================================== */

    public String generateToken(
            String userId,
            String username,
            String role
    ) {

        return Jwts.builder()
                .setSubject(userId) // userId stored here
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION
                        )
                )
                .signWith(key)
                .compact();
    }


    /* =====================================================
       GET USER ID
       ===================================================== */

    public String getUserId(String token) {

        return getClaims(token).getSubject();

    }


    /* =====================================================
       GET USERNAME (IMPORTANT FIX)
       ===================================================== */

    public String extractUsername(String token) {

        return getClaims(token)
                .get("username", String.class);

    }


    /* =====================================================
       GET ROLE
       ===================================================== */

    public String extractRole(String token) {

        return getClaims(token)
                .get("role", String.class);

    }


    /* =====================================================
       INTERNAL CLAIMS METHOD
       ===================================================== */

    private Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    /* =====================================================
   GET OWNER ID (FOR OWNER CONTROLLER)
   ===================================================== */

    public String getOwnerId(String token) {

        return getClaims(token).getSubject();

    }

}