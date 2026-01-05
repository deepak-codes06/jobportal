package com.deepak.jobportal.service;

import com.deepak.jobportal.entity.User;
import com.deepak.jobportal.exception.EmailAlreadyExistsException;
import com.deepak.jobportal.exception.InvalidCredentialsException;
import com.deepak.jobportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //SERVICE FOR REGISTERING THE NEW USER
    @Override
    public User registerUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " is already registered");
        }

        // Encrypt password using BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    //SERVICE TO LOGIN THE EXISTING USER
    @Override
    public User login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        return user;

    }

}
