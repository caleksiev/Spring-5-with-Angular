package com.fmi.spring5.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column
    @NotNull
    private String title;
    @Column
    private String description;
    @Column
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime from;
    @Column
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime to;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @NotNull
    private User organizer;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @NotNull
    private Room room;
}
