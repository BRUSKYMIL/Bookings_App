package com.reservas.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reservas.app.entity.AppUser;
import com.reservas.app.repository.AppUserRepository;

@Service
public class UserService {

    @Autowired
    private AppUserRepository userRepo;

    public Optional<AppUser> searchByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public AppUser saveUser(AppUser user) {
        return userRepo.save(user);
    }
}
