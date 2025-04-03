package com.mealmaster.mealmasterfinal.service.impl;

import com.mealmaster.mealmasterfinal.model.User;
import com.mealmaster.mealmasterfinal.repository.UserRepository;
import com.mealmaster.mealmasterfinal.service.UserService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);

    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }



}
