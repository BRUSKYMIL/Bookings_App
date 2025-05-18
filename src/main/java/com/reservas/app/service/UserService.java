package com.reservas.app.service;

import com.reservas.app.entity.AppUser;
import com.reservas.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public Optional<AppUser> buscarPorEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
