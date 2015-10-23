package com.tweet.user.repository;

import com.tweet.user.model.User;
import org.springframework.data.cassandra.repository.TypedIdCassandraRepository;

public interface UserRepository extends TypedIdCassandraRepository<User, String> {
}
