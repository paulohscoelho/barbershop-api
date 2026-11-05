package com.barbershop.api.infra.config;

import com.barbershop.api.infra.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   private final SecurityFilter securityFilter;

   public SecurityConfig(SecurityFilter securityFilter) {
      this.securityFilter = securityFilter;
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
          .cors(Customizer.withDefaults())
          .csrf(csrf -> csrf.disable())
          .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(req -> {
             req.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
             req.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll();
             req.requestMatchers(HttpMethod.POST, "/api/users").permitAll();
             req.requestMatchers(HttpMethod.GET, "/api/appointments/available-slots").permitAll();
             req.requestMatchers(HttpMethod.GET, "/api/users/barbers").hasAnyRole("CLIENT", "BARBER", "ADMIN");
             req.requestMatchers(HttpMethod.GET, "/api/services").hasAnyRole("CLIENT", "BARBER", "ADMIN");
             req.requestMatchers(HttpMethod.GET, "/api/appointments/client").hasAnyRole("CLIENT", "BARBER", "ADMIN");
             req.requestMatchers(HttpMethod.GET, "/api/appointments/barber").hasAnyRole("BARBER", "ADMIN");
             req.requestMatchers(HttpMethod.POST, "/api/appointments").hasAnyRole("CLIENT", "BARBER", "ADMIN");
             req.requestMatchers(HttpMethod.PATCH, "/api/appointments/*/cancel").hasAnyRole("CLIENT", "BARBER", "ADMIN");
             req.requestMatchers(HttpMethod.PATCH, "/api/appointments/*/complete").hasAnyRole("BARBER", "ADMIN");
             req.requestMatchers(HttpMethod.POST, "/api/working-hours").hasAnyRole("BARBER", "ADMIN");
             req.requestMatchers(HttpMethod.GET, "/api/working-hours").hasAnyRole("BARBER", "ADMIN", "CLIENT");
             req.anyRequest().authenticated();
          })
          .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
          .build();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
      return configuration.getAuthenticationManager();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}