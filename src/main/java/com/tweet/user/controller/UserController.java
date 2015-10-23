package com.tweet.user.controller;

import com.tweet.user.model.User;
import com.tweet.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("{id}")
    public ResponseEntity findById(@PathVariable String id) {
        User user = userService.findById(id);
        return new ResponseEntity(user, user != null ? OK : NOT_FOUND);
    }

    @RequestMapping(method = POST)
    public User save(@RequestBody User user, @AuthenticationPrincipal Principal token){
        user.setUsername(token.getName());
        return userService.save(user);
    }
}
