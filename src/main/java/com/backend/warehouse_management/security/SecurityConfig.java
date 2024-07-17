package com.backend.warehouse_management.security;

import com.backend.warehouse_management.enums.UserRole;
import com.backend.warehouse_management.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(POST, "/auth/register").permitAll()
                        .requestMatchers(POST, "/auth/login").permitAll()
                        //client endpoints
                        .requestMatchers(POST, "/client/**").hasAnyRole(UserRole.CLIENT.name())
                        .requestMatchers(GET, "/client/**").hasAnyRole(UserRole.CLIENT.name())
                        .requestMatchers(PUT, "/client/**").hasAnyRole(UserRole.CLIENT.name())
                        .requestMatchers(DELETE, "/client/**").hasAnyRole(UserRole.CLIENT.name())
                        //admin endpoints
                        .requestMatchers(POST, "/admin/**").hasAnyRole(UserRole.SYSTEM_ADMIN.name())
                        .requestMatchers(PUT, "/admin/**").hasAnyRole(UserRole.SYSTEM_ADMIN.name())
                        .requestMatchers(GET, "/admin/**").hasAnyRole(UserRole.SYSTEM_ADMIN.name())
                        .requestMatchers(DELETE, "/admin/**").hasAnyRole(UserRole.SYSTEM_ADMIN.name())
                        //manager endpoints
                        .requestMatchers(POST, "/manager/**").hasAnyRole(UserRole.WAREHOUSE_MANAGER.name())
                        .requestMatchers(GET, "/manager/**").hasAnyRole(UserRole.WAREHOUSE_MANAGER.name())
                        .requestMatchers(PUT, "/manager/**").hasAnyRole(UserRole.WAREHOUSE_MANAGER.name())
                        .requestMatchers(DELETE, "/manager/**").hasAnyRole(UserRole.WAREHOUSE_MANAGER.name())
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
