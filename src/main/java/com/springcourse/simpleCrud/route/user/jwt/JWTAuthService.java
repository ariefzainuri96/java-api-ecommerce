package com.springcourse.simpleCrud.route.user.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.Date;
import java.util.Map;

public class JWTAuthService {
    private final String secret;
    private final String issuer;

    public JWTAuthService(String secret, String issuer) {
        this.secret = secret;
        this.issuer = issuer;
    }

    // Generate a JWT
    public String generateToken(String username) {
        long exp = System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365);

        Map<String, Object> payload = Map.of("role", "USER");

        return JWT.create()
                .withSubject(username)
                .withPayload(payload)
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(exp))
                .sign(Algorithm.HMAC256(secret));
    }

    // Validate a JWT
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false; // Invalid token
        }
    }

    // Extract username from token
    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    public String extractRole(String token) {
        return JWT.decode(token).getClaim("role").asString();
    }
}
