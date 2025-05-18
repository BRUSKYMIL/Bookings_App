package com.reservas.app.security;

import com.reservas.app.entity.AppUser;
import com.reservas.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	AppUser user = usuarioRepo.findByEmail(email)
    	        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
