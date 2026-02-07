package com.vlu.capstone.common.security;

import com.vlu.capstone.common.config.RsaKeyProperties;
import com.vlu.capstone.common.exception.AuthException;
import com.vlu.capstone.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor

public class JwtUtil {

    private final RsaKeyProperties rsaKeys;
    

    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;
    @Value("${jwt.refresh-expiration-ms:604800000}") 
    private long refreshExpirationMs;

    public String generateToken(String email, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .claim("email", email)
                .issuedAt(now)
                .expiration(exp)
                .signWith(rsaKeys.getPrivateKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(rsaKeys.getPublicKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new AuthException(ErrorCode.AUTH_002);
        } catch (SignatureException | IllegalArgumentException e) {
            throw new AuthException(ErrorCode.AUTH_001);
        }
    }

    public String extractEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(rsaKeys.getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    // Viết thêm hàm tạo Refresh Token (thường không cần chứa nhiều claim)
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshExpirationMs);
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(exp)
                .signWith(rsaKeys.getPrivateKey())
                .compact();
    }
}
