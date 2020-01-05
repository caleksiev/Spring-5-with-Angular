package com.example.blogger.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
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
    private String id;

    @NotNull
    @NonNull
    @Size(min = 5)
    private String title;

    private User author;

  //  @NotNull
  //  @NonNull
   // @Size(min = 10)
    private String content;

    private String imageUrl;

    private String tags;

    private boolean isActive=true;
    @PastOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publicationDate = LocalDateTime.now();
}
