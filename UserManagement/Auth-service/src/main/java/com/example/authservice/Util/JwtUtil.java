package com.example.authservice.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    protected static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Sinh token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 ngày
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Xác thực token (dùng ở Gateway)
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // Lấy username từ token
    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }

    // Lấy role từ token
    public String extractRole(String token) {
        return validateToken(token).get("role", String.class);
    }

    // Kiểm tra token hết hạn
    public boolean isTokenExpired(String token) {
        return validateToken(token).getExpiration().before(new Date());
    }
}
