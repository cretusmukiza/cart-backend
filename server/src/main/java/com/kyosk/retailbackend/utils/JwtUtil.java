package com.kyosk.retailbackend.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${app.jwt.expiration_in_hours}")
    private Long  TOKEN_EXPIRATION_IN_HOURS;

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(Long userId){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        Long expirationTime = TOKEN_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
        return Jwts.builder()
                .setSubject("User")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.ES512, SECRET_KEY)
                .compact();

    }
}
