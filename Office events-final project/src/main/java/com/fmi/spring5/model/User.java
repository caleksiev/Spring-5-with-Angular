package com.fmi.spring5.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fmi.spring5.utils.Role;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JsonIgnore
    private String id;

    @Column
    @NotNull
    String firstName;

    @Column
    @NotNull
    String lastName;

    @Column
    @NotNull
    private String username;

    @Column
    @NotNull
    private String password;

    @Column
    @NotNull
    private String role = Role.DEV.getStringRole();

    public User(String fistName, String lastName, String username, String password, Role role) {
        setFirstName(fistName);
        setLastName(lastName);
        setUsername(username);
        setPassword(new BCryptPasswordEncoder(10).encode(password));
        setRole(role.getStringRole());
    }
}
