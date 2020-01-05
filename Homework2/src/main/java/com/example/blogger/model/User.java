package com.example.blogger.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document("user")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @NotNull
    @NotBlank
    @NonNull
    private String firstName;

    @NotNull
    @NonNull
    @NotBlank
    private String lastName;

    @NotNull
    @NonNull
    @Email
    private String email;

    @NotNull
    @NonNull
    private String password;

    private String role = "BLOGGER";

    private String userProfilePhoto;

    private boolean isActive = true;
}
