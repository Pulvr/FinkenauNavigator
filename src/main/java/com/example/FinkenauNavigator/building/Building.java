package com.example.FinkenauNavigator.building;

import java.util.List;

import com.example.FinkenauNavigator.room.Room;
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

    public List<Room> getRooms() {
        return rooms;
    }

}
