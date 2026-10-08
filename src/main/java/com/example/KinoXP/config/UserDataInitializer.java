package com.example.KinoXP.config;

import com.example.KinoXP.model.User;
import com.example.KinoXP.repository.UserRepository;
import com.example.KinoXP.utils.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDataInitializer {

    @Bean
    CommandLineRunner seedUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${kino.admin.username}") String adminUsername,
            @Value("${kino.admin.password}") String adminPassword,
            @Value("${kino.movie-operator.username}") String movieOperatorUsername,
            @Value("${kino.movie-operator.password}") String movieOperatorPassword,
            @Value("${kino.reservation.username}") String reservationUsername,
            @Value("${kino.reservation.password}") String reservationPassword
    ) {
        return args -> {
            createIfMissing(userRepository, passwordEncoder, adminUsername, adminPassword, UserRole.ADMIN);
            createIfMissing(userRepository, passwordEncoder, movieOperatorUsername, movieOperatorPassword,
                    UserRole.MOVIE_OPERATOR);
            createIfMissing(userRepository, passwordEncoder, reservationUsername, reservationPassword,
                    UserRole.RESERVATION);
        };
    }

    private void createIfMissing(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String username,
            String password,
            UserRole role
    ) {
        if (userRepository.findByUsername(username).isEmpty()) {
            userRepository.save(new User(username, passwordEncoder.encode(password), role));
        }
    }
}
