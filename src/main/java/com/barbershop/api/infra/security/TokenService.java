package com.barbershop.api.infra.security;

import com.barbershop.api.domain.entities.User;
import com.barbershop.api.domain.exceptions.RegraNegocioException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import java.util.Date;

@Service
public class TokenService {

   @Value("${api.security.token.secret}")
   private String secret;

   public String generateToken(User user) {
      try {
         javax.crypto.SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

         return Jwts.builder()
             .issuer("barbershop-api")
             .subject(user.getEmail())
             .claim("role", user.getRole().name())
             .issuedAt(new Date())
             .expiration(new Date(System.currentTimeMillis() + 7200000))
             .signWith(key)
             .compact();
      } catch (Exception e) {
         throw new RegraNegocioException("Erro ao gerar o token JWT.");
      }
   }

   public String validateToken(String token) {
      try {
         javax.crypto.SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

         Claims claims = Jwts.parser()
             .verifyWith(key)
             .build()
             .parseSignedClaims(token)
             .getPayload();

         return claims.getSubject();
      } catch (Exception e) {
         throw new RegraNegocioException("Token JWT inválido ou expirado.");
      }
   }
}