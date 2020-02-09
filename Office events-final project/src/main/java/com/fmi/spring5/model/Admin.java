package com.fmi.spring5.model;

import com.fmi.spring5.utils.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @NotNull
    private User user;

    public Admin(String firstName, String lastName, String username, String password) {
        setUser(new User(firstName, lastName, username, password, Role.ADMIN));
    }

    public Admin(User user, boolean crypt) {
        user.setRole(Role.ADMIN.getStringRole());
        if (crypt) {
            user.setPassword(new BCryptPasswordEncoder(10).encode(user.getPassword()));
        }
        setUser(user);
    }
}
