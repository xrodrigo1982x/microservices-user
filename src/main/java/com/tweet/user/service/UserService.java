package com.tweet.user.service;

import com.tweet.user.model.User;
import com.tweet.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "user.user", key = "#id")
    public User findById(String id) {
        return userRepository.findOne(id);
    }

    @CacheEvict(value = {"user.user", "tweet.user", "application.user", "follow.user"}, key = "#user.username")
    public User save(User user) {
        return userRepository.save(user);
    }

}
