// package com.hlopg_backend.security;

// import java.util.Date;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;

// @Component
// public class JwtUtil {

//     @Value("${jwt.secret}")
//     private String jwtSecret;

//     @Value("${jwt.expiration}")
//     private long jwtExpiration;

//     public String generateToken(Long userId, String role) {
//         return Jwts.builder()
//                 .setSubject(String.valueOf(userId))
//                 .claim("role", role)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                 .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
//                 .compact();
//     }
// }


package com.hlopg_backend.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // ===================== KEY =====================
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ===================== GENERATE TOKEN =====================
    public String generateToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ===================== EXTRACT ALL CLAIMS =====================
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ===================== EXTRACT USER ID =====================
    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).getSubject());
    }

    // ===================== EXTRACT ROLE =====================
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ===================== CHECK EXPIRATION =====================
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // ===================== VALIDATE TOKEN =====================
    // public boolean validateToken(String token) {
    //     try {
    //         extractAllClaims(token);
    //         return true;
    //     } catch (Exception e) {
    //         return false;
    //     }
    // }
    public boolean validateToken(String token) {
    try {
        Claims claims = extractAllClaims(token);
        return !claims.getExpiration().before(new Date());
    } catch (Exception e) {
        return false;
    }
}

}
