package com.mealmaster.mealmasterfinal.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.mealmaster.mealmasterfinal.model.User;

public interface UserService extends UserDetailsService {

    boolean existsByUsername(String username);

    void saveUser(User user);


    // Future custom methods for user service can be added here
}
