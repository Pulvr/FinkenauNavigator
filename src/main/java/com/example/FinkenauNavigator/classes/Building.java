package com.example.FinkenauNavigator.classes;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("BUILDING")
public class Building {
    @Id
    private int id;
    private String name;
    private List<Room> rooms;

    public Building() {

    }

    public Building(String name ,List<Room> rooms) {
        this.name = name;
        this.rooms = rooms;
    }

}
