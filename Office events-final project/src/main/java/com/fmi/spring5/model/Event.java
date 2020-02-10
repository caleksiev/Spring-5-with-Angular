package com.fmi.spring5.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
abstract public class Event {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column
    @NotNull
    private String title;
    @Column
    private String description;
    @Column(name = "valueFrom")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",timezone = "GMT")
    private Date fromDate;
    @Column(name = "valueTo")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    private Date toDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NotNull
    private User organizer;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NotNull
    private Room room;
}
