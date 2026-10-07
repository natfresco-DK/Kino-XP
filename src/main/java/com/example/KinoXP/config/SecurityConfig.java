package com.example.KinoXP.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/CSS/**", "/JS/**", "/images/**", "/error").permitAll()
                        .requestMatchers("/program", "/movies/**", "/api/movies", "/api/movies/**")
                        .hasAnyRole("ADMIN", "MOVIE_OPERATOR")

                        .requestMatchers(HttpMethod.GET, "/screenings", "/screenings/date/**", "/screenings/movie/**")
                        .hasAnyRole("ADMIN", "MOVIE_OPERATOR")
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/program", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder,
            @Value("${kino.admin.username}") String username,
            @Value("${kino.admin.password}") String password,
            @Value("${kino.movie-operator.username}") String movieOperatorUsername,
            @Value("${kino.movie-operator.password}") String movieOperatorPassword
    ) {
        UserDetails admin = User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("ADMIN")
                .build();

        UserDetails movieOperator = User.withUsername(movieOperatorUsername)
                .password(passwordEncoder.encode(movieOperatorPassword))
                .roles("MOVIE_OPERATOR")
                .build();

        return new InMemoryUserDetailsManager(admin, movieOperator);
    }
}
