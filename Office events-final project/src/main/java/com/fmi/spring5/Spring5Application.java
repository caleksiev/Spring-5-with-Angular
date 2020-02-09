package com.fmi.spring5;

import com.fmi.spring5.model.Admin;
import com.fmi.spring5.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Spring5Application {

    public static void main(String[] args) {
        SpringApplication.run(Spring5Application.class, args);
    }

    @Bean
    public CommandLineRunner run(AdminRepository adminRepository) {
        return (args) -> {
            Admin user = new Admin("Admin","Admin","admin","admin");

            if (adminRepository.count() == 0) {
                adminRepository.save(user);
            }
        };
    }

}
