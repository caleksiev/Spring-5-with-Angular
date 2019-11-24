package com.example.blogger.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Document("user")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    String id;

    @NotNull
    @NonNull
    String firstName;

    @NotNull
    @NonNull
    String lastName;

    @NotNull
    @NonNull
    @Email
    String email;

    @NotNull
    @NonNull
    String password;

    @NotNull
    @NonNull
    String role;

    String userProfilePhoto;
}
