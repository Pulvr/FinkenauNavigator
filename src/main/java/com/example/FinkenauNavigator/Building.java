package com.example.FinkenauNavigator;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("BUILDING")
public class Building {
    @Id
    private int id;

    private String name;

    public Building() {

    }

    public Building(String name) {
        this.name = name;
    }

}
