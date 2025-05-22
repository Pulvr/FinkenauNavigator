package com.example.FinkenauNavigator;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("BUILDING")
public class Building {
    @Id
    private long id;

    private String name;

    private String floor;

    public Building() {

    }

    public Building(String name, String floor) {
        this.name = name;
        this.floor = floor;
    }

}
