package br.com.api.desafio.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final String secret = "b4f9c2d1e8a9f4b7c6d3e2f1a0b9c8d7";
    private final long validityInMillis = 1000L * 60 * 60 * 24;

    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String subject, String role, UUID userId){
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .claim("uid", userId.toString())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validityInMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException ex){return false;}
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public String getEmail(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String getRole(String token) {
        return getClaim(token, c -> c.get("role", String.class));
    }

    public String getUserId(String token) {
        return getClaim(token, c -> c.get("uid", String.class));
    }

    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }
}
