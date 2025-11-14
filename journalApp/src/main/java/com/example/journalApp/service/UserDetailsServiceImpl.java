package com.example.journalApp.service;

import com.example.journalApp.entity.UserModel;
import com.example.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserModel> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        UserModel dbUser = user.get();

        return User.builder()
                .username(dbUser.getUsername())
                .password(dbUser.getPassword())
                .roles(dbUser.getRoles().toArray(new String[0]))
                .build();
    }
}
