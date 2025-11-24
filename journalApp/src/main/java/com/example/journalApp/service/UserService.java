package com.example.journalApp.service;

import com.example.journalApp.entity.UserModel;
import com.example.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final BCryptPasswordEncoder  passwordEncoder = new BCryptPasswordEncoder();


    public void saveEntity(UserModel user) {
        userRepository.save(user);
    }

    //called for normal user registration
    public void saveNewUser(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        user.setActive();
        userRepository.save(user);
    }


    //admin creation methods
    public void saveNewUserAsAdmin(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("ADMIN"));
        user.setActive();
        userRepository.save(user);
    }


    public List<UserModel> getAll() {
        return userRepository.findAll();
    }

    public Optional<UserModel> findById(String id) {
        return userRepository.findById(String.valueOf(new ObjectId(id)));
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(String.valueOf(id));
    }

    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
