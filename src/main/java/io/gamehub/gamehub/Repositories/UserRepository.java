package io.gamehub.gamehub.Repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import io.gamehub.gamehub.Model.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {

}
