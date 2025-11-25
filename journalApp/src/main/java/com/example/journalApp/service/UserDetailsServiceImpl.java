package com.example.journalApp.service;

import com.example.journalApp.entity.UserModel;
import com.example.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<UserModel> user = Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email)));

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        UserModel dbUser = user.get();

        if (!dbUser.isActive()) {
            throw new DisabledException("User is disabled");
        }

        String[] roles = dbUser.getRoles() == null || dbUser.getRoles().isEmpty() ?
                new String[]{"USER"} :
                dbUser.getRoles().toArray(new String[0]);

        return User.builder()
                .username(dbUser.getEmail())
                .password(dbUser.getPassword())
                .roles(roles)
                .disabled(!dbUser.isActive())
                .build();
    }
}
