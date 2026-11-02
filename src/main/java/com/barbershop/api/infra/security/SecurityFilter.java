package com.barbershop.api.infra.security;

import com.barbershop.api.domain.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

   private final TokenService tokenService;
   private final UserRepository userRepository;

   public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
      this.tokenService = tokenService;
      this.userRepository = userRepository;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      var tokenJWT = recuperarToken(request);

      if (tokenJWT != null) {
         var subject = tokenService.validateToken(tokenJWT);
         UserDetails user = userRepository.findByEmail(subject).orElse(null);

         if (user != null) {
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
         }
      }

      filterChain.doFilter(request, response);
   }

   private String recuperarToken(HttpServletRequest request) {
      var authorizationHeader = request.getHeader("Authorization");
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
         return authorizationHeader.replace("Bearer ", "").trim();
      }
      return null;
   }
}