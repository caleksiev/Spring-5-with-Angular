package com.example.blogger;

import com.example.blogger.dao.UserRepository;
import com.example.blogger.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
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