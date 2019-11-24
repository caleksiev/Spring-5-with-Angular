package com.example.blogger.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Document("post")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    String id;

    @NotNull
    @NonNull
    @Size(min = 5)
    String title;

    @NotNull
    @NonNull
    User author;

    @NotNull
    @NonNull
    @Size(min = 10)
    String content;

    String imageUrl;

    List<String> tags;

    boolean isActive = true;

    LocalDateTime publicationDate = LocalDateTime.now();
}
