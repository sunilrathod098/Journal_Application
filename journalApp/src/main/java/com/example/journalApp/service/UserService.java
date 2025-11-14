package com.example.journalApp.service;

import com.example.journalApp.entity.UserModel;
import com.example.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveEntity(UserModel user) {
        userRepository.save(user);
    }

    public void saveNewUser(UserModel user) {
        userRepository.save(user);
    }

    public List<UserModel> getAll() {
        return userRepository.findAll();
    }

    public Optional<UserModel> findById(String id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
