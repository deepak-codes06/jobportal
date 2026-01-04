package com.deepak.jobportal.controller;

import com.deepak.jobportal.dto.LoginRequest;
import com.deepak.jobportal.entity.User;
import com.deepak.jobportal.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request){
        return userService.login(request.getEmail(), request.getPassword());
    }


}
