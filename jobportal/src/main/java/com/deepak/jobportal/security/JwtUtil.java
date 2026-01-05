package com.deepak.jobportal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    //Secret key ( Used to sign token )
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token validity (10 hours)
    private static final long EXPIRATION_TIME = 10 * 60 * 60 * 1000;

    //Generate JWT token
    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    //Extract the Email from the token
    public String extractEmail(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();


        return claims.getSubject();
    }


    // For Validating the token
    public boolean isTokenValid(String token){
        try {
            extractEmail(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
