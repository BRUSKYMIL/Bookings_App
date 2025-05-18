package com.reservas.app.controller;

import com.reservas.app.dto.LoginRequest;
import com.reservas.app.dto.LoginResponse;
import com.reservas.app.entity.AppUser;
import com.reservas.app.repository.UserRepository;
import com.reservas.app.security.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        AppUser user = userRepo.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
        	    .withUsername(user.getEmail())
        	    .password(user.getPassword())
        	    .roles(user.getRole())
        	    .build();

        String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
