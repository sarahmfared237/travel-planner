package com.Sarah.travel_planner.service;

import com.Sarah.travel_planner.dto.AuthRequest;
import com.Sarah.travel_planner.dto.AuthResponse;
import com.Sarah.travel_planner.dto.RegisterRequest;
import com.Sarah.travel_planner.model.Role;
import com.Sarah.travel_planner.model.User;
import com.Sarah.travel_planner.repository.UserRepository;
import com.Sarah.travel_planner.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // First user ever registered becomes admin
        boolean isFirstUser = userRepository.count() == 0;
        Role role = isFirstUser ? Role.ROLE_ADMIN : Role.ROLE_USER;

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setWantToVisitDestinationIds(new ArrayList<>());

        userRepository.save(user);

        System.out.println(">> New user registered: " + user.getUsername()
                + " with role: " + role.name());

        String token = jwtUtil.generateToken(user.getUsername(), role.name());
        return new AuthResponse(token, role.name(), user.getUsername());
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), user.getUsername());
    }
}