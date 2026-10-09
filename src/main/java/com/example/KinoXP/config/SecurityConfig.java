package com.example.KinoXP.config;

import com.example.KinoXP.utils.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/error",
                                "/program",
                                "/screenings",
                                "/screenings/date/**",
                                "/screenings/movie/**",
                                "/api/movies"
                        )
                        .permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/movies/**",
                                "/api/movies/**"
                                )
                            .hasAnyRole(UserRole.ADMIN.name(),UserRole.MOVIE_OPERATOR.name())

                        .requestMatchers(HttpMethod.PUT, "/api/movies/**")
                            .hasAnyRole(UserRole.ADMIN.name(), UserRole.MOVIE_OPERATOR.name())

                        .requestMatchers("/kiosk/**")
                            .hasAnyRole(UserRole.ADMIN.name(), UserRole.RESERVATION.name())

                        .anyRequest().hasRole(UserRole.ADMIN.name())
                )
                .formLogin(form -> form
                        .successHandler((request, response, authentication) -> {
                            if (authentication.getAuthorities().stream()
                                    .anyMatch(authority -> authority.getAuthority()
                                            .equals("ROLE_" + UserRole.RESERVATION.name()))) {
                                response.sendRedirect("/kiosk/sell");
                            } else {
                                response.sendRedirect("/program");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
