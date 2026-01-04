package com.deepak.jobportal.service;

import com.deepak.jobportal.entity.User;

public interface UserService {
    User registerUser(User user);
    User login(String email, String password);
}
