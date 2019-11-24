package com.example.blogger;

import com.example.blogger.dao.UserRepository;
import com.example.blogger.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BloggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloggerApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(UserRepository userRepository) {
        return (args) -> {
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setFirstName("ADMIN");
            user.setLastName("ADMIN");
            user.setPassword(new BCryptPasswordEncoder(10).encode("admin"));
            user.setRole("ADMIN");

            if (userRepository.count() == 0) {
                userRepository.save(user);
            }
        };
    }

}