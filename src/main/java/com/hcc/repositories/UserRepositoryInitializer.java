package com.hcc.repositories;
import com.hcc.controllers.AuthenticationController;
import com.hcc.entities.User;
import com.hcc.utils.CustomPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class UserRepositoryInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    CustomPasswordEncoder customPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Override
    public void run(String... args) {

        if (userRepo.count() == 0) {
            LocalDate date = LocalDate.now();

            User user1 = new User(date, "agustin.biondi@gmail.com", "{bcrypt}"+customPasswordEncoder.getPasswordEncoder().encode("1234"));
            User user2 = new User(date, "admin", "{bcrypt}"+customPasswordEncoder.getPasswordEncoder().encode("123"));
            List<User> users = Arrays.asList(user1, user2);
            userRepo.saveAll(users);
        }
    }
}

