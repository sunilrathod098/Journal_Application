package com.example.journalApp.repository;

import com.example.journalApp.entity.UserModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);

//    Optional<UserModel> findById(String userId);/
}
