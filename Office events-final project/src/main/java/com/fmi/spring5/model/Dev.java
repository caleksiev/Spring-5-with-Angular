package com.fmi.spring5.model;


import com.fmi.spring5.utils.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Dev {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @NotNull
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Team team;

    public Dev(String fistName,String lastName,String username, String password) {
        setUser(new User(fistName,lastName,username, password, Role.DEV));
    }

    public Dev(User user) {
        user.setRole(Role.DEV.getStringRole());
        setUser(user);
    }
}
