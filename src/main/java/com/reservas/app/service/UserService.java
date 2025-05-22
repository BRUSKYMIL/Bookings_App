package com.reservas.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservas.app.entity.AppUser;
import com.reservas.app.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public Optional<AppUser> searchByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
