package com.fmi.spring5.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TeamEvent extends Event {
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Team team;
}
