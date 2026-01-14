package com.deepak.jobportal.controller;

import com.deepak.jobportal.dto.LoginRequest;
import com.deepak.jobportal.entity.User;
import com.deepak.jobportal.security.JwtUtil;
import com.deepak.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        User user = userService.login(request.getEmail(), request.getPassword());

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(token);
    }

    @GetMapping("/profile")
    public String profile(){
        return "JWT IS WORKING";
    }


}
