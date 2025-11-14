package com.example.journalApp.repository;

import com.example.journalApp.entity.UserModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, ObjectId> {
    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findById(ObjectId id);

    Optional<UserModel> findById(String id);
}
