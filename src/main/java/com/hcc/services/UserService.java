package com.hcc.services;

import com.hcc.entities.User;
import com.hcc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createNewUser(User user) {
        userRepository.save(user);
        return user;
    }

    public Page<User> getAllUsers(Pageable p) {
        return userRepository.findAll(p);
    }


}
