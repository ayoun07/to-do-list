package com.todo.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clé secrète (à ne pas hardcoder en prod), utilisée pour signer les tokens
    private static final String SECRET_KEY = "12345678901234567890123456789012";

    // Durée de validité du token : ici 10 heures
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    // Clé cryptographique générée à partir de la clé secrète
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Génère un token JWT en y mettant le nom d'utilisateur comme "subject"
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrait le nom d'utilisateur (subject) contenu dans un token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Vérifie que le token appartient bien à l'utilisateur donné
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }
}
