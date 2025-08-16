package com.movieflix.service;

import com.movieflix.dto.request.LoginRequest;
import com.movieflix.dto.response.JwtResponse;
import com.movieflix.model.User;
import com.movieflix.repository.UserRepository;
import com.movieflix.security.JwtService;
import com.movieflix.security.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${movieflix.jwt.expiration}")
    private long jwtExpiration;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
                       UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticate user and generate JWT token
     */
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        UserDetailsServiceImpl.UserPrincipal userPrincipal =
                (UserDetailsServiceImpl.UserPrincipal) authentication.getPrincipal();

        // Update last login time
        User user = userPrincipal.getUser();
        user.updateLastLogin();
        userRepository.save(user);

        // Generate JWT token
        String jwt = jwtService.generateToken(userPrincipal);

        logger.info("User {} authenticated successfully", user.getUsername());

        return new JwtResponse(jwt, user.getUsername(), user.getRoles(), jwtExpiration / 1000);
    }

    /**
     * Create default admin user if not exists
     */
    public void createDefaultAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@movieflix.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(List.of("ADMIN", "USER"));
            admin.setActive(true);

            userRepository.save(admin);
            logger.info("Default admin user created");
        }
    }

    /**
     * Register new user (for future use)
     */
    public User registerUser(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(List.of("USER"));
        user.setActive(true);

        return userRepository.save(user);
    }
}
