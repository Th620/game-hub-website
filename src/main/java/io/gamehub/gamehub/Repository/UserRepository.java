package io.gamehub.gamehub.Repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import io.gamehub.gamehub.Model.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
